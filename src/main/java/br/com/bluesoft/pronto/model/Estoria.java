package br.com.bluesoft.pronto.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Estoria {

	@Id
	private int estoriaKey;
	private String titulo;
	private String descricao;

	public int getEstoriaKey() {
		return estoriaKey;
	}

	public void setEstoriaKey(int estoriaKey) {
		this.estoriaKey = estoriaKey;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
