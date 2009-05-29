package br.com.bluesoft.pronto.core;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TipoDeTicket {

	public static final int IDEIA = 1;
	public static final int ESTORIA = 2;
	public static final int DEFEITO = 3;
	public static final int IMPEDIMENTO = 5;
	public static final int TAREFA = 6;
	
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

	public TipoDeTicket(int tipoDeTicketKey) {
		super();
		this.tipoDeTicketKey = tipoDeTicketKey;
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
