package com.bsp.bspmobility.model;

import java.util.List;

public class RotasEOnibusResponse {

	private List<RotaComOnibus> rotas;
	private String rotaId;
	
	public RotasEOnibusResponse(List<RotaComOnibus> rotas, String rotaId) {
		this.rotas = rotas;
		this.rotaId = rotaId;
	}

	public List<RotaComOnibus> getRotas() {
		return rotas;
	}

	public void setRotas(List<RotaComOnibus> rotas) {
		this.rotas = rotas;
	}

	public String getRotaId() {
		return rotaId;
	}

	public void setRotaId(String rotaId) {
		this.rotaId = rotaId;
	}
	
	
}
