package com.bsp.bspmobility.model;

import java.util.List;

import com.bsp.bspmobility.projections.DenunciaProjection;

public class RotaComOnibus {

	private OpcaoRota rota;
	private List<OnibusInfo> onibusInfos;
	private List<DenunciaProjection> denuncias;
	
	public RotaComOnibus(OpcaoRota rota, List<OnibusInfo> onibusInfos, List<DenunciaProjection> denuncias) {
		this.rota = rota;
		this.onibusInfos = onibusInfos;
		this.denuncias = denuncias != null ? denuncias : List.of();
	}

	public OpcaoRota getRota() {
		return rota;
	}

	public List<OnibusInfo> getOnibusInfos() {
		return onibusInfos;
	}

	public List<DenunciaProjection> getDenuncias() {
		return denuncias;
	}
}
