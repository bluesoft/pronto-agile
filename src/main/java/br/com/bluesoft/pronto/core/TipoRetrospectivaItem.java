package br.com.bluesoft.pronto.core;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TipoRetrospectivaItem {

	public static final int WWW = 1;
	public static final int WCBI = 2;

	@Id
	private int tipoRetrospectivaItemKey;

	private String descricao;

	public int getTipoRetrospectivaItemKey() {
		return tipoRetrospectivaItemKey;
	}

	public void setTipoRetrospectivaItemKey(final int tipoRetrospectivaItemKey) {
		this.tipoRetrospectivaItemKey = tipoRetrospectivaItemKey;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

}
