package br.com.bluesoft.pronto.core;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TipoDeTicket {

	public static int ESTORIA = 1;
	public static int DEFEITO = 1;
	public static int TAREFA = 1;

	@Id
	private int tipoDeTicketKey;

	private String descricao;

	public TipoDeTicket() {

	}

	public TipoDeTicket(int tipoDeTicketKey, String descricao) {
		super();
		this.tipoDeTicketKey = tipoDeTicketKey;
		this.descricao = descricao;
	}

	public int getTipoDeTicketKey() {
		return tipoDeTicketKey;
	}

	public void setTipoDeTicketKey(int tipoDeTicketKey) {
		this.tipoDeTicketKey = tipoDeTicketKey;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
