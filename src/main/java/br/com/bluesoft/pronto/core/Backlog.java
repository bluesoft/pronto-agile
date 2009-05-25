package br.com.bluesoft.pronto.core;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Backlog {

	public static final int IDEIAS = 1;

	public static final int PRODUCT_BACKLOG = 2;

	public static final int SPRINT_BACKLOG = 3;

	public static final int LIXEIRA = 4;

	@Id
	private int tipoDeBacklogKey;

	private String descricao;

	public int getTipoDeBacklogKey() {
		return tipoDeBacklogKey;
	}

	public void setTipoDeBacklogKey(final int tipoDeBacklogKey) {
		this.tipoDeBacklogKey = tipoDeBacklogKey;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

}
