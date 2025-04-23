	package com.bsp.bspmobility.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bsp.bspmobility.dto.DenunciaDTO;
import com.bsp.bspmobility.entities.Denuncia;
import com.bsp.bspmobility.projections.DenunciaProjection;
import com.bsp.bspmobility.repository.DenunciaRepository;

@Service
public class DenunciaService {
	
	@Autowired
	private DenunciaRepository denunciaRepository;
	
	private final Path rootLocation = Paths.get("uploads");
	
	public DenunciaService() {
		try {
			Files.createDirectories(rootLocation);
		} catch (IOException e) {
			throw new RuntimeException("Não pode inicializar o armazenamento ", e);
		}
	}
	
	public Denuncia salvar(Denuncia denuncia, MultipartFile imagem) {
		if (imagem != null && !imagem.isEmpty()) {
			try {
				String fileName = UUID.randomUUID().toString() + "_" + imagem.getOriginalFilename();
				Path destinationFile = rootLocation.resolve(fileName).normalize().toAbsolutePath();
				Files.copy(imagem.getInputStream(), destinationFile);
				denuncia.setImagemPath(fileName);
			} catch (IOException e) {
				throw new RuntimeException("Falha em Armazenar imagem ", e);
			}
		}
		return denunciaRepository.save(denuncia);
	}
	
	public List<DenunciaDTO> listar(){
	    List<Denuncia> result = denunciaRepository.findAll();
	    return result.stream().map(DenunciaDTO::new).toList();
	}
	
	@Transactional(readOnly = true)
	public List<DenunciaDTO> findByLinha(String linha) {
		List<DenunciaProjection> denuncia = denunciaRepository.findByLinha(linha);
		return denuncia.stream().map(DenunciaDTO::new).toList();
	}
}
