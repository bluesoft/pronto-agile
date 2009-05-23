package br.com.bluesoft.pronto.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Ticket {

	@Id
	private int ticketKey;

	private String titulo;

	private String descricao;

	public int getTicketKey() {
		return ticketKey;
	}

	public void setTicketKey(final int ticketKey) {
		this.ticketKey = ticketKey;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(final String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

}
