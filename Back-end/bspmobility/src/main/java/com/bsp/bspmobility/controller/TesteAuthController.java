package com.bsp.bspmobility.controller;

import org.apache.hc.core5.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bsp.bspmobility.service.AuthService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/teste-auth")
public class TesteAuthController {
	
	private static final Logger logger = LoggerFactory.getLogger(TesteAuthController.class);
	
	private AuthService authService;

	public TesteAuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@GetMapping
	public Mono<String> teste_auth(){
		return authService.autenticar()
				.map(autenticado -> {
					if(autenticado) {
						String cookie = authService.getHeaderAutenticado().getFirst(HttpHeaders.COOKIE);
						logger.info("Autenticação feita com sucesso! Cookie {}", cookie);
						return "Autenticação bem sucedida! Cookie" + cookie;
					}
					logger.warn("Falha na autenticação no controller");
	                return "Autenticação Falhou";
				})
				
				.onErrorReturn("Erro Durante a Autenticação");
	}
}