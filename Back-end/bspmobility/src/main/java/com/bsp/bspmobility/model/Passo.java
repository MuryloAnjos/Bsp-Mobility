package com.bsp.bspmobility.model;

public class Passo {

	private String modoViagem; //Walking ou Transit
	private String instrucao; // ex Va para a rua tal, vire onde
	private String distancia;
	private String duracao;
	private String linhaOnibus;
	
	public Passo(String modoViagem, String instrucao, String distancia, String duracao, String linhaOnibus) {
		this.modoViagem = modoViagem;
		this.instrucao = instrucao;
		this.distancia = distancia;
		this.duracao = duracao;
		this.linhaOnibus = linhaOnibus;
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

	public String getLinhaOnibus() {
		return linhaOnibus;
	}
	
	
	
}
