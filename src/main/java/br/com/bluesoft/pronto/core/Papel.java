package br.com.bluesoft.pronto.core;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Papel {

	public Papel() {

	}

	public Papel(int papelKey) {
		this.papelKey = papelKey;
	}

	@Id
	private int papelKey;

	private String descricao;

	public int getPapelKey() {
		return papelKey;
	}

	public void setPapelKey(int papelKey) {
		this.papelKey = papelKey;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
