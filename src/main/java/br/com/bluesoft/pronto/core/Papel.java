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

	public Papel(final int papelKey) {
		this.papelKey = papelKey;
	}

	public Papel(final int papelKey, final String descricao) {
		this.papelKey = papelKey;
		this.descricao = descricao;
	}

	@Id
	private int papelKey;

	private String descricao;

	public int getPapelKey() {
		return papelKey;
	}

	public void setPapelKey(final int papelKey) {
		this.papelKey = papelKey;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + papelKey;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Papel other = (Papel) obj;
		if (papelKey != other.papelKey) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return descricao;
	}

}
