package com.bsp.bspmobility.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bsp.bspmobility.model.OnibusInfo;
import com.bsp.bspmobility.model.OpcaoRota;
import com.bsp.bspmobility.model.RotasEOnibusResponse;
import com.bsp.bspmobility.service.RotaService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/rotas")
@CrossOrigin(origins = "*")
public class RotaController {

	private final RotaService rotaService;
	
	public RotaController(RotaService rotaService) {
		this.rotaService = rotaService;
	}
	
	@GetMapping("/rotas-e-onibus")
	public Mono<ResponseEntity<RotasEOnibusResponse>> obterRotasEOnibus(@RequestParam String origem,
			@RequestParam String destino, @RequestParam(required = false) String rotaId){
		return rotaService.obterRotasEOnibus(origem, destino, rotaId)
				.map(ResponseEntity::ok)
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/opcoes")
	public Flux<ResponseEntity<OpcaoRota>> getRotas(@RequestParam String origem,
			@RequestParam String destino){
		return rotaService.buscarRotasMaps(origem, destino)
				.map(ResponseEntity::ok)
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/onibus-por-rota")
	public Flux<ResponseEntity<OnibusInfo>> getOnibusPorRota(@RequestParam String origem,
			@RequestParam String destino){
		return rotaService.obterInfoOnibusPorRota(origem, destino)
				.map(ResponseEntity::ok)
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
}
