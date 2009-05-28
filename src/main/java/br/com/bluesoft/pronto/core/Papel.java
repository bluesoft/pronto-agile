package br.com.bluesoft.pronto.core;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Papel {

	public static final int PRODUCT_OWNER = 1;
	public static final int SCRUM_MASTER = 2;
	public static final int DESENVOLVEDOR = 3;
	public static final int TESTADOR = 4;
	public static final int SUPORTE = 5;

	public Papel() {

	}

	public Papel(int papelKey) {
		this.papelKey = papelKey;
	}

	public Papel(int papelKey, String descricao) {
		this.papelKey = papelKey;
		this.descricao = descricao;
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
