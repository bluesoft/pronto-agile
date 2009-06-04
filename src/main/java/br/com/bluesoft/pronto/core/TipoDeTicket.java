package br.com.bluesoft.pronto.core;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TipoDeTicket {

	public static final int IDEIA = 1;
	public static final int ESTORIA = 2;
	public static final int DEFEITO = 3;
	public static final int TAREFA = 6;

	@Id
	private int tipoDeTicketKey;

	private String descricao;

	public TipoDeTicket() {

	}

	public TipoDeTicket(final int tipoDeTicketKey, final String descricao) {
		super();
		this.tipoDeTicketKey = tipoDeTicketKey;
		this.descricao = descricao;
	}

	public TipoDeTicket(final int tipoDeTicketKey) {
		super();
		this.tipoDeTicketKey = tipoDeTicketKey;
	}

	public int getTipoDeTicketKey() {
		return tipoDeTicketKey;
	}

	public void setTipoDeTicketKey(final int tipoDeTicketKey) {
		this.tipoDeTicketKey = tipoDeTicketKey;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return descricao;
	}

}
