package com.bsp.bspmobility.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bsp.bspmobility.entities.Denuncia;
import com.bsp.bspmobility.repository.DenunciaRepository;

@Service
public class DenunciaService {
	
	@Autowired
	private DenunciaRepository denunciaRepository;
	
	public Denuncia salvar(Denuncia denuncia) {
		return denunciaRepository.save(denuncia);
	}

}
