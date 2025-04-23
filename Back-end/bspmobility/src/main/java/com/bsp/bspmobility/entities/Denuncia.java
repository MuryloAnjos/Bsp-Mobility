package com.bsp.bspmobility.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "denuncias")
public class Denuncia {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String linha;
	private String problema;
	private LocalDateTime dataHora;
	private String prefixo;
	private String placa;
	private String empresa;
	
	@Column(nullable = true)
	private String imagemPath; 
	
	@PrePersist
	protected void onCreate() {
	   this.dataHora = LocalDateTime.now();
	}
		
	public Denuncia() {
		
	}

	public Denuncia(int id, String linha, String problema, LocalDateTime dataHora, String prefixo, String placa,
			String empresa, String imagemPath) {
		super();
		this.id = id;
		this.linha = linha;
		this.problema = problema;
		this.dataHora = dataHora;
		this.prefixo = prefixo;
		this.placa = placa;
		this.empresa = empresa;
		this.imagemPath = imagemPath;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLinha() {
		return linha;
	}

	public void setLinha(String linha) {
		this.linha = linha;
	}

	public String getProblema() {
		return problema;
	}

	public void setProblema(String problema) {
		this.problema = problema;
	}

	public LocalDateTime getDataHora() {
		return dataHora;
	}

	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}

	public String getPrefixo() {
		return prefixo;
	}

	public void setPrefixo(String prefixo) {
		this.prefixo = prefixo;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	
	public String getImagemPath() {
		return imagemPath;
	}

	public void setImagemPath(String imagemPath) {
		this.imagemPath = imagemPath;
	}
}
