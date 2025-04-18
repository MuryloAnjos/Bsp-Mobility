package com.bsp.bspmobility.model;

import java.time.Instant;

public class OnibusInfo {

	private String minutos;
	private String linha;
	private String nome;
	private String sentido;
	private String numeroCarro;
	private Boolean acessivel;
	private Instant ultimaAtualizacao;
	
	public OnibusInfo(String minutos, String linha, String nome, String sentido, String numeroCarro, Boolean acessivel) {
		this.minutos = minutos;
		this.linha = linha;
		this.nome = nome;
		this.sentido = sentido;
		this.numeroCarro = numeroCarro;
		this.acessivel = acessivel;
		this.ultimaAtualizacao = Instant.now();
	}

	public String getMinutos() {
		return minutos;
	}

	public void setMinutos(String minutos) {
		this.minutos = minutos;
	}
	
	public String getLinha() {
		return linha;
	}

	public void setLinha(String linha) {
		this.nome = linha;
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

	public Instant getUltimaAtualizacao() {
		return ultimaAtualizacao;
	}

	public void setUltimaAtualizacao(Instant ultimaAtualizacao) {
		this.ultimaAtualizacao = ultimaAtualizacao;
	}
	
    public void atualizarMinutos() {
    	if(minutos != null && minutos.matches("\\d+") ) {
    		long minutosPassados = (Instant.now().getEpochSecond() - ultimaAtualizacao.getEpochSecond() / 60);
    		int minutosAtuais = Integer.parseInt(minutos);
    		int novosMinutos = Math.max(0, minutosAtuais - (int) minutosPassados);
    		this.minutos = novosMinutos > 0 ? String.valueOf(novosMinutos) : "Ônibus já está chegando";
    		this.ultimaAtualizacao = Instant.now();
    	}else {
    		this.minutos = "não há previsões";
    	}
    }
	
	
}
