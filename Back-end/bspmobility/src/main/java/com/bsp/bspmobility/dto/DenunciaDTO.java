package com.bsp.bspmobility.dto;

import com.bsp.bspmobility.entities.Denuncia;

public class DenunciaDTO {

	private String linha;
	private String problema;
	private int prefixo;
	private String empresa;
	private String imagemPath; 

	public DenunciaDTO() {
		
	}
	
	public DenunciaDTO(Denuncia entity) {
		linha = entity.getLinha();
		problema = entity.getProblema();
		prefixo = entity.getPrefixo();
		empresa = entity.getEmpresa();
		imagemPath = entity.getImagemPath();
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
	public int getPrefixo() {
		return prefixo;
	}
	public void setPrefixo(int prefixo) {
		this.prefixo = prefixo;
	}
	public String getEmpresa() {
		return empresa;
	}
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	public String getImagemPath() {
		return imagemPath;
	}
	public void setImagemPath(String imagemPath) {
		this.imagemPath = imagemPath;
	}
}
