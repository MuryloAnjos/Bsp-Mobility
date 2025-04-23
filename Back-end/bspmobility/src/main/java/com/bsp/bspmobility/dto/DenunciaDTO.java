package com.bsp.bspmobility.dto;

import java.time.LocalDateTime;

import com.bsp.bspmobility.entities.Denuncia;
import com.bsp.bspmobility.projections.DenunciaProjection;

public class DenunciaDTO {

	private String linha;
	private String problema;
	private String prefixo;
	private String imagemPath;
	private LocalDateTime dataHora;

	public DenunciaDTO() {
		
	}
	
	public DenunciaDTO(Denuncia entity) {
		linha = entity.getLinha();
		problema = entity.getProblema();
		prefixo = entity.getPrefixo();
		imagemPath = entity.getImagemPath();
		dataHora = entity.getDataHora();
	}
	
	public DenunciaDTO(DenunciaProjection projection) {
		linha = projection.getLinha();
		problema = projection.getProblema();
		prefixo = projection.getPrefixo();
		imagemPath = projection.getImagem_path();
		dataHora = projection.getDataHora();
	}
	
	public String getLinha() {
		return linha;
	}
	public void setLinha(String linha) {
		this.linha = linha;
	}
	public String getProblema() {
		return problema;
	}
	public void setProblema(String problema) {
		this.problema = problema;
	}
	public String getPrefixo() {
		return prefixo;
	}
	public void setPrefixo(String prefixo) {
		this.prefixo = prefixo;
	}
	public String getImagemPath() {
		return imagemPath;
	}
	public void setImagemPath(String imagemPath) {
		this.imagemPath = imagemPath;
	}
	public LocalDateTime getDataHora() {
		return dataHora;
	}
	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}
}
