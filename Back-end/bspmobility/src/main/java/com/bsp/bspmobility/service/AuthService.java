package com.bsp.bspmobility.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class AuthService {

	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
	
	private final WebClient webClient;
	private final String token;
	private String authCookie;
	
	public AuthService() {
		this.token = "6f64a0310d7d12a1b7356fe3b04377c926589cf7e2735455cb1600dfeec0ca1c";
		this.webClient = WebClient.builder()
				.baseUrl("http://api.olhovivo.sptrans.com.br/v2.1")
				.defaultHeader(HttpHeaders.ACCEPT, "application/json")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
				.codecs(configurer -> configurer
	                    .defaultCodecs()
	                    .maxInMemorySize(10 * 1024 * 1024)) // 10MB
				.build();
	}
	
	public Mono<Boolean> autenticar(){
		return webClient.post()
				.uri(uriBuilder -> uriBuilder
						.path("/Login/Autenticar")
						.queryParam("token", token)
						.build())
				.retrieve()
				.toBodilessEntity()
				.flatMap(response -> {
					String cookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
					
					if(cookie != null) {
						logger.info("Autenticação feita com sucesso! Seu Cookie {} ", cookie);
						this.authCookie = cookie;
						return Mono.just(true);
					}
					
					logger.warn("Autenticação falhou");
					return Mono.just(false);
				})
				.onErrorResume(throwable -> {
					logger.error("Erro na Autenticação : " + throwable.getMessage());
					return Mono.just(false);
				});
	
	}
	
	public HttpHeaders getHeaderAutenticado() {
		if (authCookie == null) {
            throw new IllegalStateException("O método autenticar não foi chamado");
        }
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.COOKIE, authCookie);
		return headers;
	
	}
	
	public WebClient getWebClientOlhoVivo() {
		return webClient;
	}
	
}
