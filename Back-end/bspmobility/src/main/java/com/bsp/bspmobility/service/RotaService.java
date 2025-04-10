package com.bsp.bspmobility.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.bsp.bspmobility.model.OpcaoRota;
import com.bsp.bspmobility.model.Passo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;

@Service
public class RotaService {

	private static final Logger logger = LoggerFactory.getLogger(RotaService.class);

	private final WebClient webClientGoogleMaps;
	private final AuthService authService;
	private ObjectMapper objectMapper = new ObjectMapper();
	private final String googleApiKey = "AIzaSyB1aR6w22MI2HjTGHcAHDQmsHB0xGrFhUs";

	public RotaService(AuthService authService) {
		this.authService = authService;
		this.webClientGoogleMaps = WebClient.builder().baseUrl("https://maps.googleapis.com")
				.defaultHeader(HttpHeaders.COOKIE, "application/json").build();
	}
	
	public Flux<OpcaoRota> buscarRotasMaps(String origem, String destino){
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
				.flatMapMany(json -> this.parseRotas(json))
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
				List<Passo> passos = parsePassos(leg.path("steps"));
				opcoesRota.add(new OpcaoRota(duracao, distancia, horaSaida, horaChegada, passos));
			}
			return Flux.fromIterable(opcoesRota);
		} catch (Exception e){
			logger.error("Erro ao parsear as respostas do Google Maps {} ", e.getMessage());
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
			String linhaOnibus = "TRANSIT".equals(modoViagem)
					? passo.path("transit_details").path("line").path("short_name").asText()
					: null;
			passos.add(new Passo(modoViagem, instrucao, distancia, duracao, linhaOnibus));
		}
		return passos;
	}
}
