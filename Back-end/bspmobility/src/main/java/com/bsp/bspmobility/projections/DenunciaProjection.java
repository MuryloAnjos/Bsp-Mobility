package com.bsp.bspmobility.projections;

import java.time.LocalDateTime;

public interface DenunciaProjection {

	String getLinha();
	String getPrefixo();
	String getProblema();
	String getImagem_path();
	LocalDateTime getDataHora();
	
}
