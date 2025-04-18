package com.bsp.bspmobility.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bsp.bspmobility.dto.DenunciaDTO;
import com.bsp.bspmobility.entities.Denuncia;
import com.bsp.bspmobility.service.DenunciaService;

@RestController
@RequestMapping("/denuncias")
public class DenunciaController {
	
	@Autowired
	private DenunciaService denunciaService;
	
	@PostMapping(consumes = {"multipart/form-data"})
	public ResponseEntity<Denuncia> salvar(
			@RequestPart("denuncia") Denuncia denuncia,
			@RequestPart(value = "imagem", required = false) MultipartFile imagem) {
		Denuncia savedDenuncia = denunciaService.salvar(denuncia, imagem);
		return ResponseEntity.ok(savedDenuncia);
	}
	
	@GetMapping
	public List<DenunciaDTO> listar(){
		List<DenunciaDTO> result = denunciaService.listar();
		return result;
	}
}
