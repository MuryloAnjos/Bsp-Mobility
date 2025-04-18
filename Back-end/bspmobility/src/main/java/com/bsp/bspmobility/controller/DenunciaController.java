package com.bsp.bspmobility.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bsp.bspmobility.dto.DenunciaDTO;
import com.bsp.bspmobility.entities.Denuncia;
import com.bsp.bspmobility.service.DenunciaService;

@RestController
@RequestMapping("/denuncias")
public class DenunciaController {
	
	@Autowired
	private DenunciaService denunciaService;
	
	@PostMapping
	public ResponseEntity<Denuncia> salvar(@RequestBody Denuncia denuncia) {
		Denuncia salva = denunciaService.salvar(denuncia);
		return ResponseEntity.status(HttpStatus.CREATED).body(salva);
	}
	
	@GetMapping
	public ResponseEntity<List<DenunciaDTO>> listar(){
		List<DenunciaDTO> result = denunciaService.listar();
		return ResponseEntity.ok(result);
	}
	
}
