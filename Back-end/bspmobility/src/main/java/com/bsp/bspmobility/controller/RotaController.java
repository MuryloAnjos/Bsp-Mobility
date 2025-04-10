package com.bsp.bspmobility.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bsp.bspmobility.model.OpcaoRota;
import com.bsp.bspmobility.service.RotaService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/rotas")
public class RotaController {

	private final RotaService rotaService;
	
	public RotaController(RotaService rotaService) {
		this.rotaService = rotaService;
	}
	
	@GetMapping("/opcoes")
	public Flux<ResponseEntity<OpcaoRota>> getRotas(@RequestParam String origem,
			@RequestParam String destino){
		return rotaService.buscarRotasMaps(origem, destino)
				.map(ResponseEntity::ok)
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
}
