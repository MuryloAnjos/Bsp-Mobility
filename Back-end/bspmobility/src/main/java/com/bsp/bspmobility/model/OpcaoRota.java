package com.bsp.bspmobility.model;

import java.util.List;

public class OpcaoRota {
	private String duracao;
	private String distancia;
	private String horaSaida;
	private String horaChegada;
	private List<Passo> passos;
	
	public OpcaoRota(String duracao, String distancia, String horaSaida, String horaChegada, List<Passo> passos) {
		this.duracao = duracao;
		this.distancia = distancia;
		this.horaSaida = horaSaida;
		this.horaChegada = horaChegada;
		this.passos = passos;
	}

	public String getDuracao() {
		return duracao;
	}

	public String getDistancia() {
		return distancia;
	}

	public String getHoraSaida() {
		return horaSaida;
	}

	public String getHoraChegada() {
		return horaChegada;
	}

	public List<Passo> getPassos() {
		return passos;
	}
	
	

}
