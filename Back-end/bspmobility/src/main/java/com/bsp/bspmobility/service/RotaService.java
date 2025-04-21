package com.bsp.bspmobility.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import com.bsp.bspmobility.model.OnibusInfo;
import com.bsp.bspmobility.model.OpcaoRota;
import com.bsp.bspmobility.model.ParadaLinha;
import com.bsp.bspmobility.model.Passo;
import com.bsp.bspmobility.model.RotaComOnibus;
import com.bsp.bspmobility.model.RotasEOnibusResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RotaService {

    private static final Logger logger = LoggerFactory.getLogger(RotaService.class);

    private final WebClient webClientGoogleMaps;
    private final AuthService authService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${google.maps.api.key}")
    private String googleApiKey;
    private static final double VELOCIDADE_MEDIA_KMH = 16.0;
    private static final int MINUTOS_MAXIMO = 60;
    private static final double DISTANCIA_MAXIMA_KM = 10.0;
    private final Cache<String, RotasEOnibusResponse> rotaCache;

    public RotaService(AuthService authService) {
        this.authService = authService;
        this.webClientGoogleMaps = WebClient.builder()
                .baseUrl("https://maps.googleapis.com")
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .build();
        this.rotaCache = Caffeine.newBuilder() // gerencia caches
        		.expireAfterWrite(1, TimeUnit.HOURS) // expira em 1 horinha
        		.maximumSize(1000)
        		.build();
    }
    
    public String gerarRotaId(String origem, String destino) {
    	return UUID.randomUUID().toString();
    }
     
    public Mono<RotasEOnibusResponse> obterRotasEOnibus(String origem, String destino, String rotaId) {
    	
        if (origem == null || origem.trim().isEmpty() || destino == null || destino.trim().isEmpty()) {
            logger.error("Origem ou destino inválidos: origem {}, destino {}", origem, destino);
            return Mono.just(new RotasEOnibusResponse(
                    List.of(new RotaComOnibus(null, List.of(new OnibusInfo("não há previsões", "", "", "", "", false)))),
                    null));
        }


        if (rotaId != null && !rotaId.isEmpty()) {
            // Busca resposta no cache
            RotasEOnibusResponse cachedResponse = rotaCache.getIfPresent(rotaId);
            if (cachedResponse != null) {
                logger.info("Atualizando minutos para rotaId: {}", rotaId);
                // Atualiza minutos de todos os OnibusInfo
                List<RotaComOnibus> rotasAtualizadas = cachedResponse.getRotas().stream()
                        .map(rotaComOnibus -> {
                            List<OnibusInfo> onibusInfos = rotaComOnibus.getOnibusInfos().stream()
                                    .peek(OnibusInfo::atualizarMinutos) // Chama atualizarMinutos
                                    .filter(onibus -> !onibus.getMinutos().equals("ônibus chegou")) // Remove ônibus que chegaram
                                    .collect(Collectors.toList());
                            return new RotaComOnibus(rotaComOnibus.getRota(), onibusInfos);
                        })
                        .collect(Collectors.toList());

                return Mono.just(new RotasEOnibusResponse(rotasAtualizadas, rotaId));
            } else {
                logger.warn("RotaId {} não encontrada no cache", rotaId); 
               
                return Mono.just(new RotasEOnibusResponse(
                        List.of(new RotaComOnibus(null, List.of(new OnibusInfo("não há previsões", "", "", "", "", false)))),
                        null));
            }
        }


        String novoRotaId = gerarRotaId(origem, destino);
        return buscarRotasMaps(origem, destino)
        		
                .flatMap(rota -> {
                    List<ParadaLinha> linhas = extrairNumerosLinhas(rota);
                    if (linhas.isEmpty()) {
                        logger.warn("Nenhuma linha de ônibus encontrada para a rota: {}", rota.getDuracao());
                        return Mono.just(new RotaComOnibus(rota, List.of(new OnibusInfo("não há previsões", "", "", "", "", false))));
                    }
                    // Busca detalhes dos ônibus
                    return Flux.fromIterable(linhas)
                            .flatMap(this::buscarDetalhesOnibus)
                            .distinct() // Remove duplicatas
                            .collectList()
                            .map(onibusInfos -> new RotaComOnibus(rota, onibusInfos)); // Associa à rota
                })
                .collectList()
                .map(rotasComOnibus -> {
                    // Cria resposta com rotas e rotaId
                    RotasEOnibusResponse response = new RotasEOnibusResponse(rotasComOnibus, novoRotaId);
                    // Armazena no cache
                    rotaCache.put(novoRotaId, response);
                    logger.info("Rota armazenada no cache com rotaId: {}", novoRotaId);
                    return response;
                })
                .onErrorResume(e -> {
                    logger.error("Erro ao obter rotas e ônibus para {} a {}: {}", origem, destino, e.getMessage());
                    // Retorna resposta com não há previsões
                    return Mono.just(new RotasEOnibusResponse(
                            List.of(new RotaComOnibus(null, List.of(new OnibusInfo("não há previsões", "", "", "", "", false)))),
                            null));
                });
    }

    public Flux<OpcaoRota> buscarRotasMaps(String origem, String destino) {
        return webClientGoogleMaps.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/maps/api/directions/json")
                        .queryParam("origin", origem)
                        .queryParam("destination", destino)
                        .queryParam("mode", "transit")
                        .queryParam("transit_routing_preference", "fewer_transfers")
                        .queryParam("alternatives", "true")
                        .queryParam("language", "pt-BR")
                        .queryParam("departure_time", "now")
                        .queryParam("key", googleApiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .flatMapMany(this::parseRotas)
                .doOnNext(rota -> logger.info("Rota encontrada: {}", rota.getDuracao()));
    }

    private Flux<OpcaoRota> parseRotas(String googleResponse) {
        try {
            JsonNode rota = objectMapper.readTree(googleResponse);
            JsonNode rotas = rota.path("routes");
            List<OpcaoRota> opcoesRota = new ArrayList<>();
            for (JsonNode opcao : rotas) {
                JsonNode leg = opcao.path("legs").get(0);
                String duracao = leg.path("duration").path("text").asText();
                String distancia = leg.path("distance").path("text").asText();
                String horaSaida = leg.path("departure_time").path("text").asText();
                String horaChegada = leg.path("arrival_time").path("text").asText();
                String endComeco = leg.path("start_address").asText();
                String endFinal = leg.path("end_address").asText();
                List<Passo> passos = parsePassos(leg.path("steps"));
                opcoesRota.add(new OpcaoRota(duracao, distancia, horaSaida, horaChegada, endComeco, endFinal, passos));
            }
            return Flux.fromIterable(opcoesRota);
        } catch (Exception e) {
            logger.error("Erro ao parsear resposta do Google Maps: {}", e.getMessage());
            return Flux.empty();
        }
    }

    private List<Passo> parsePassos(JsonNode passosNode) {
        List<Passo> passos = new ArrayList<>();
        for (JsonNode passo : passosNode) {
            String modoViagem = passo.path("travel_mode").asText();
            String instrucao = passo.path("html_instructions").asText();
            String distancia = passo.path("distance").path("text").asText();
            String duracao = passo.path("duration").path("text").asText();
            String nomeOnibus = "TRANSIT".equals(modoViagem)
                    ? passo.path("transit_details").path("line").path("name").asText()
                    : null;
            String linhaOnibus = "TRANSIT".equals(modoViagem)
                    ? passo.path("transit_details").path("line").path("short_name").asText()
                    : null;
            Integer numeroParadas = "TRANSIT".equals(modoViagem)
                    ? passo.path("transit_details").path("num_stops").asInt()
                    : null;
            String nomeParadaComeco = "TRANSIT".equals(modoViagem)
                    ? passo.path("transit_details").path("departure_stop").path("name").asText()
                    : null;
            String nomeParadaFinal = "TRANSIT".equals(modoViagem)
                    ? passo.path("transit_details").path("arrival_stop").path("name").asText()
                    : null;
            Double latParada = "TRANSIT".equals(modoViagem)
                    ? passo.path("transit_details").path("departure_stop").path("location").path("lat").asDouble(0.0)
                    : null;
            Double lngParada = "TRANSIT".equals(modoViagem)
                    ? passo.path("transit_details").path("departure_stop").path("location").path("lng").asDouble(0.0)
                    : null;
            passos.add(new Passo(modoViagem, instrucao, distancia, duracao, nomeOnibus, linhaOnibus, numeroParadas, nomeParadaComeco, nomeParadaFinal, latParada, lngParada));
        }
        return passos;
    }

    private List<ParadaLinha> extrairNumerosLinhas(OpcaoRota rota) {
        List<ParadaLinha> linhas = new ArrayList<>();
        for (Passo passo : rota.getPassos()) {
            if ("TRANSIT".equals(passo.getModoViagem()) && passo.getLinhaOnibus() != null && passo.getNomeParadaComeco() != null) {
                String linha = passo.getLinhaOnibus();
                String parada = passo.getNomeParadaComeco();
                Double latParada = passo.getLatParada();
                Double lngParada = passo.getLngParada();
                ParadaLinha paradaLinha = new ParadaLinha(linha, parada, latParada, lngParada);
                if (!linhas.contains(paradaLinha)) {
                    linhas.add(paradaLinha);
                }

            }
        }
        return linhas;
    }

    
    private boolean isAntesDoPonto(double latParada, double lngParada, double latVeiculo, double lngVeiculo, int sentido) {
    	double deltaLat = latVeiculo - latParada;
    	double deltaLng = lngVeiculo - lngParada;
    	if (sentido == 1) { // Ida
    		return deltaLat <= 0 || deltaLng <= 0; // Ônibus ao sul ou oeste do ponto
    	} else { // Volta
    		return deltaLat >= 0 || deltaLng >= 0; // Ônibus ao norte ou leste do ponto
    	}
    }
    
    private double calcularDistancia(double lon1, double lat1, double lon2, double lat2) {
    	double R = 6371.0;
    	double dLat = Math.toRadians(lat2 - lat1);
    	double dLon = Math.toRadians(lon2 - lon1);
    	double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    			Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
    			Math.sin(dLon / 2) * Math.sin(dLon / 2);
    	double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    	return R * c;
    }
    
    private Mono<OnibusInfo> buscarPrevisaoPorPosicaoComCoordenadas(double latParada, double lngParada, String numeroLinha) {
        return authService.getWebClientOlhoVivo().get()
                .uri("/Posicao")
                .headers(headers -> headers.addAll(authService.getHeaderAutenticado()))
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(posicaoResponse -> {
                    try {
                        JsonNode rootPosicao = objectMapper.readTree(posicaoResponse);
                        JsonNode linhas = rootPosicao.path("l");
                        for (JsonNode linha : linhas) {
                            String codigoLinha = linha.path("c").asText();
                            
                            if (numeroLinha.equalsIgnoreCase(codigoLinha)) {
           
                                String nomeLinha = linha.path("lt0").asText() + " / " +
                                        linha.path("lt1").asText();
                                
                                int sentido = linha.path("sl").asInt();
                                String sentidoStr = sentido == 1 ? "Ida" : "Volta";
                                JsonNode veiculos = linha.path("vs");
                                if (veiculos.isArray() && !veiculos.isEmpty()) {
                                    List<OnibusInfo> onibusInfos = new ArrayList<>();
                                    for (JsonNode veiculo : veiculos) {
                                        double latVeiculo = veiculo.path("py").asDouble();
                                        double lngVeiculo = veiculo.path("px").asDouble();
                                        boolean antesDoPonto = isAntesDoPonto(
                                                latParada, lngParada, latVeiculo, lngVeiculo, sentido);
                                        if (antesDoPonto) {
                                            double distancia = calcularDistancia(
                                                    lngParada, latParada, lngVeiculo, latVeiculo);
                                            if (distancia <= DISTANCIA_MAXIMA_KM) {
                                                double minutos = (distancia / VELOCIDADE_MEDIA_KMH) * 60.0;
                                                String minutosStr = String.valueOf(
                                                        (int) Math.min(Math.round(minutos), MINUTOS_MAXIMO));
                                                onibusInfos.add(new OnibusInfo(
                                                        minutosStr,
                                                        codigoLinha,
                                                        nomeLinha,
                                                        sentidoStr,
                                                        veiculo.path("p").asText(),
                                                        veiculo.path("a").asBoolean()
                                                ));
                                            }
                                        }
                                    }
                                    if (!onibusInfos.isEmpty()) {
                                        OnibusInfo maisProximo = onibusInfos.stream()
                                                .min((o1, o2) -> Integer.compare(
                                                        Integer.parseInt(o1.getMinutos()),
                                                        Integer.parseInt(o2.getMinutos())))
                                                .orElse(null);
                                        logger.info("Ônibus encontrado para linha {}: {} minutos",
                                                numeroLinha, maisProximo.getMinutos());
                                        return Mono.just(maisProximo);
                                    }
                                    logger.warn("Nenhum ônibus antes do ponto encontrado para linha {}", numeroLinha);
                                    return Mono.just(new OnibusInfo("Nenhum ônibus à caminho", codigoLinha, numeroLinha, "", "", false));
                                }
                                logger.warn("Nenhum veículo encontrado para linha {}", numeroLinha);
                                return Mono.empty();
                            }
                        }
                        logger.warn("Linha {} não encontrada em /Posicao", numeroLinha);
                        
                        return Mono.just(new OnibusInfo("Sem Previsões", numeroLinha , numeroLinha, "", "", false));
                    } catch (Exception e) {
                        logger.error("Erro ao parsear resposta de /Posicao: {}", e.getMessage());
                        return Mono.empty();
                    }
                })
                .onErrorResume(e -> {
                    logger.error("Erro na chamada à /Posicao para linha {}: {}", numeroLinha, e.getMessage());
                    return Mono.empty();
                });
    }

    private Mono<OnibusInfo> buscarPrevisaoPorPosicaoComGeocoding(String endereco, String numeroLinha) {
        return webClientGoogleMaps.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/maps/api/geocode/json")
                        .queryParam("address", endereco)
                        .queryParam("key", googleApiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(geocodeResponse -> {
                    try {
                        JsonNode root = objectMapper.readTree(geocodeResponse);
                        JsonNode location = root.path("results").get(0).path("geometry").path("location");
                        double latParada = location.path("lat").asDouble();
                        double lngParada = location.path("lng").asDouble();
                        return buscarPrevisaoPorPosicaoComCoordenadas(latParada, lngParada, numeroLinha);
                    } catch (Exception e) {
                        logger.error("Erro ao geocodificar endereço {}: {}", endereco, e.getMessage());
                        return Mono.empty();
                    }
                });
    }


    public Mono<OnibusInfo> buscarDetalhesOnibus(ParadaLinha paradaLinha) {
    	
    	String numeroLinha = paradaLinha.getLinhaOnibus();
    	String nomeParada = paradaLinha.getNomeParada();
    	Double latParada = paradaLinha.getLatParada();
    	Double lngParada = paradaLinha.getLngParada();
    	
    	if (latParada == null || lngParada == null) {
    		logger.warn("Coordenadas não disponíveis para parada: {}. Revertendo para geocodificação.", nomeParada);
    		return buscarPrevisaoPorPosicaoComGeocoding(nomeParada, numeroLinha);
    	}
    	
    	return authService.autenticar()
    			.<OnibusInfo>flatMap(autenticado -> {  
    				if (autenticado) {
    					return buscarPrevisaoPorPosicaoComCoordenadas(latParada, lngParada, numeroLinha);
    				} else {
    					return Mono.error(new ResponseStatusException(
    							HttpStatus.UNAUTHORIZED,
    							"Falha na autenticação para a linha: " + numeroLinha
    							));
    				}
    			})
    			.switchIfEmpty(Mono.error(new ResponseStatusException(
    					HttpStatus.NOT_FOUND,
    					"Nenhuma previsão de ônibus encontrada para a linha " + numeroLinha + " na parada " + nomeParada
    					)));
    }
    
    public Flux<OnibusInfo> obterInfoOnibusPorRota(String origem, String destino) {
        return buscarRotasMaps(origem, destino)
                .flatMap(googleResponse -> {
                    List<ParadaLinha> linhas = extrairNumerosLinhas(googleResponse);
                    if (linhas.isEmpty()) {
                        logger.warn("Nenhuma linha de ônibus encontrada para a rota de {} a {}", origem, destino);
                        return Flux.empty();
                    }
                    logger.info("Linhas encontradas para a rota de {} a {}: {}", origem, destino, linhas);
                    return Flux.fromIterable(linhas)
                            .flatMap(this::buscarDetalhesOnibus)
                            .distinct();
                })
                .onErrorResume(e -> {
                    logger.error("Erro ao obter informações de ônibus para a rota de {} a {}: {}", 
                            origem, destino, e.getMessage());
                    return Flux.empty();
                });
    }
    
}