package com.bsp.bspmobility.model;

public class OnibusInfo {

	private String minutos;
	private String nome;
	private String sentido;
	private String numeroCarro;
	private Boolean acessivel;
	
	public OnibusInfo(String minutos, String nome, String sentido, String numeroCarro, Boolean acessivel) {
		this.minutos = minutos;
		this.nome = nome;
		this.sentido = sentido;
		this.numeroCarro = numeroCarro;
		this.acessivel = acessivel;
	}

	public String getMinutos() {
		return minutos;
	}

	public void setMinutos(String minutos) {
		this.minutos = minutos;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSentido() {
		return sentido;
	}

	public void setSentido(String sentido) {
		this.sentido = sentido;
	}

	public String getNumeroCarro() {
		return numeroCarro;
	}

	public void setNumeroCarro(String numeroCarro) {
		this.numeroCarro = numeroCarro;
	}

	public Boolean getAcessivel() {
		return acessivel;
	}

	public void setAcessivel(Boolean acessivel) {
		this.acessivel = acessivel;
	}
	
	
}
