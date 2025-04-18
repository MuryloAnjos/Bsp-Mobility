package com.bsp.bspmobility.model;

import java.util.List;

public class RotaComOnibus {

	private OpcaoRota rota;
	private List<OnibusInfo> onibusInfos;
	
	public RotaComOnibus(OpcaoRota rota, List<OnibusInfo> onibusInfos) {
		this.rota = rota;
		this.onibusInfos = onibusInfos;
	}

	public OpcaoRota getRota() {
		return rota;
	}

	public void setRota(OpcaoRota rota) {
		this.rota = rota;
	}

	public List<OnibusInfo> getOnibusInfos() {
		return onibusInfos;
	}

	public void setOnibusInfos(List<OnibusInfo> onibusInfos) {
		this.onibusInfos = onibusInfos;
	}
	
	
}
