package br.com.bluesoft.pronto.core

import java.util.List

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Transient

import br.com.bluesoft.pronto.model.Ticket

@Entity
@org.hibernate.annotations.Entity(mutable = false)
public class Backlog {

	public static final int IDEIAS = 1
	public static final int PRODUCT_BACKLOG = 2
	public static final int SPRINT_BACKLOG = 3
	public static final int LIXEIRA = 4
	public static final int IMPEDIMENTOS = 5

	Backlog() {

	}

	Backlog(final int backlogKey) {
		this.backlogKey = backlogKey
	}

	Backlog(final int backlogKey, final String descricao) {
		this.backlogKey = backlogKey
		this.descricao = descricao
	}

	@Id
	int backlogKey

	String descricao

	@OneToMany(mappedBy = "backlog")
	List<Ticket> tickets

	public String toString() {
		return descricao
	}

	public boolean isSprintBacklog() {
		return backlogKey == SPRINT_BACKLOG
	}

	public List<Ticket> getTickets() {
		return tickets
	}
	
	@Transient
	double getEsforcoTotal() {
		double total = 0
		if (tickets != null) {
			for (final Ticket ticket : tickets) {
				if (ticket.isDefeito() || ticket.isEstoria() || ticket.isIdeia()) {
					total += ticket.getEsforco()
				}
			}
		}
		return total
	}
	
	@Transient
	int getValorDeNegocioTotal() {
		int total = 0
		if (tickets != null) {
			for (final Ticket ticket : tickets) {
				if (ticket.isDefeito() || ticket.isEstoria() || ticket.isIdeia()) {
					total += ticket.getValorDeNegocio()
				}
			}
		}
		return total
	}
	
	@Transient
	Integer getTempoDeVidaMedioEmDias() {
		int total = 0
		int quantidade = 0
		if (tickets != null) {
			for (final Ticket ticket : tickets) {
				if (ticket.isDefeito() || ticket.isEstoria() || ticket.isIdeia()) {
					quantidade++
					total += ticket.getTempoDeVidaEmDias()
				}
			}
		}
		return total / quantidade
	}
}
