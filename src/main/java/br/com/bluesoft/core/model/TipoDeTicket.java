package br.com.bluesoft.core.model;

import javax.persistence.Entity;

@Entity
public class TipoDeTicket {

	public static int ESTORIA = 1;
	public static int DEFEITO = 1;
	public static int TAREFA = 1;

	private int tipoDeTicketKey;

	private String descricao;

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
