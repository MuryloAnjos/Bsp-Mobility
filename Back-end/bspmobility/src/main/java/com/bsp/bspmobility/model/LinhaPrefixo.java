package com.bsp.bspmobility.model;

public class LinhaPrefixo {
	
	private String linhaOnibus;
	private String prefixo;
	
	public LinhaPrefixo() {
		
	}

	public LinhaPrefixo(String linhaOnibus, String prefixo) {
		this.linhaOnibus = linhaOnibus;
		this.prefixo = prefixo;
	}

	public String getLinhaOnibus() {
		return linhaOnibus;
	}

	public void setLinhaOnibus(String linhaOnibus) {
		this.linhaOnibus = linhaOnibus;
	}

	public String getPrefixo() {
		return prefixo;
	}

	public void setPrefixo(String prefixo) {
		this.prefixo = prefixo;
	}
	
	

}
