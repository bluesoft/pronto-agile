package br.com.bluesoft.pronto.core;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import br.com.bluesoft.pronto.model.Ticket;

@Entity
public class Backlog {

	public static final int IDEIAS = 1;
	public static final int PRODUCT_BACKLOG = 2;
	public static final int SPRINT_BACKLOG = 3;
	public static final int LIXEIRA = 4;
	public static final int IMPEDIMENTOS = 5;

	public Backlog() {

	}

	public Backlog(final int backlogKey) {
		this.backlogKey = backlogKey;
	}

	public Backlog(final int backlogKey, final String descricao) {
		this.backlogKey = backlogKey;
		this.descricao = descricao;
	}

	@Id
	private int backlogKey;

	private String descricao;

	@OneToMany(mappedBy = "backlog")
	private List<Ticket> tickets;

	public int getBacklogKey() {
		return backlogKey;
	}

	public void setBacklogKey(final int backlogKey) {
		this.backlogKey = backlogKey;
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

	public boolean isSprintBacklog() {
		return backlogKey == SPRINT_BACKLOG;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public int getEsforcoTotal() {
		int total = 0;
		if (tickets != null) {
			for (final Ticket ticket : tickets) {
				if (ticket.isDefeito() || ticket.isEstoria() || ticket.isIdeia()) {
					total += ticket.getEsforco();
				}
			}
		}
		return total;
	}

	public int getValorDeNegocioTotal() {
		int total = 0;
		if (tickets != null) {
			for (final Ticket ticket : tickets) {
				if (ticket.isDefeito() || ticket.isEstoria() || ticket.isIdeia()) {
					total += ticket.getValorDeNegocio();
				}
			}
		}
		return total;
	}
}
