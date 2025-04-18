package com.bsp.bspmobility.model;

public class Passo {

	private final String modoViagem; //Walking ou Transit
	private final String instrucao; // ex Va para a rua tal, vire onde
	private final String distancia;
	private final String duracao;
	private final String nomeOnibus;
	private final String linhaOnibus;
	private final Integer numeroParadas;
	private final String nomeParadaComeco;
	private final String nomeParadaFinal;
	private final Double latParada;
	private final Double lngParada;
	
	public Passo(String modoViagem, String instrucao, String distancia, String duracao, String nomeOnibus,
			String linhaOnibus, Integer numeroParadas, String nomeParadaComeco, String nomeParadaFinal,
			Double latParada, Double lngParada) {
		this.modoViagem = modoViagem;
		this.instrucao = instrucao;
		this.distancia = distancia;
		this.duracao = duracao;
		this.nomeOnibus = nomeOnibus;
		this.linhaOnibus = linhaOnibus;
		this.numeroParadas = numeroParadas;
		this.nomeParadaComeco = nomeParadaComeco;
		this.nomeParadaFinal = nomeParadaFinal;
		this.latParada = latParada;
		this.lngParada = lngParada;
	}

	public String getModoViagem() {
		return modoViagem;
	}

	public String getInstrucao() {
		return instrucao;
	}

	public String getDistancia() {
		return distancia;
	}

	public String getDuracao() {
		return duracao;
	}

	public String getNomeOnibus() {
		return nomeOnibus;
	}

	public String getLinhaOnibus() {
		return linhaOnibus;
	}

	public Integer getNumeroParadas() {
		return numeroParadas;
	}

	public String getNomeParadaComeco() {
		return nomeParadaComeco;
	}

	public String getNomeParadaFinal() {
		return nomeParadaFinal;
	}

	public Double getLatParada() {
		return latParada;
	}

	public Double getLngParada() {
		return lngParada;
	}
	
	
	
	
	
	
	
}
