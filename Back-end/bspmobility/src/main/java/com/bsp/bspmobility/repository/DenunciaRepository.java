package com.bsp.bspmobility.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bsp.bspmobility.entities.Denuncia;
import com.bsp.bspmobility.projections.DenunciaProjection;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, Integer>{
	
	@Query(nativeQuery = true, value = """
			SELECT linha, problema, prefixo, imagem_path, data_hora 
			FROM DENUNCIAS
			WHERE linha = :linha
			""")
	List<DenunciaProjection> findByLinha(String linha);
	
	@Query(nativeQuery = true, value = """
			SELECT linha, problema, prefixo, imagem_path, data_hora 
			FROM DENUNCIAS
			WHERE (:linha IS NULL OR linha = :linha)
			AND (:prefixo IS NULL OR prefixo = :prefixo)
			""")
	List<DenunciaProjection> findByLinhaEPrefixo(String linha, String prefixo);
	
}
