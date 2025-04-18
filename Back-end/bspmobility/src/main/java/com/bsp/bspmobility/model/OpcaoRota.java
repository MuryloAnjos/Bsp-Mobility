package com.bsp.bspmobility.model;

import java.util.List;

public class OpcaoRota {
	private String duracao;
	private String distancia;
	private String horaSaida;
	private String horaChegada;
	private String endComeco;
	private String endFinal;
	private List<Passo> passos;
	
	public OpcaoRota(String duracao, String distancia, String horaSaida, String horaChegada, String endComeco, String endFinal, List<Passo> passos) {
		this.duracao = duracao;
		this.distancia = distancia;
		this.horaSaida = horaSaida;
		this.horaChegada = horaChegada;
		this.endComeco = endComeco;
		this.endFinal = endFinal;
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
	
	public String getEndComeco() {
		return endComeco;
	}
	
	public String getEndFinal() {
		return endFinal;
	}

	public List<Passo> getPassos() {
		return passos;
	}
	
	

}
