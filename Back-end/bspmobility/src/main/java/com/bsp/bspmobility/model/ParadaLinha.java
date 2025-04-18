package com.bsp.bspmobility.model;

public class ParadaLinha {
    private final String linhaOnibus;
    private final String nomeParada;
    private final Double latParada; // Novo campo
    private final Double lngParada; // Novo campo

    public ParadaLinha(String linhaOnibus, String nomeParada, Double latParada, Double lngParada) {
        this.linhaOnibus = linhaOnibus;
        this.nomeParada = nomeParada;
        this.latParada = latParada;
        this.lngParada = lngParada;
    }

    public String getLinhaOnibus() {
        return linhaOnibus;
    }

    public String getNomeParada() {
        return nomeParada;
    }

    public Double getLatParada() {
        return latParada;
    }

    public Double getLngParada() {
        return lngParada;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParadaLinha that = (ParadaLinha) o;
        return linhaOnibus.equals(that.linhaOnibus) && nomeParada.equals(that.nomeParada);
    }

    @Override
    public int hashCode() {
        return 31 * linhaOnibus.hashCode() + nomeParada.hashCode();
    }
}