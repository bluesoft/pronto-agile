/*
 * Copyright 2009 Pronto Agile Project Management.
 * This file is part of Pronto.
 * Pronto is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Pronto is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Pronto. If not, see <http://www.gnu.org/licenses/>.
 */

package br.com.bluesoft.pronto.model

import java.math.RoundingMode;
import java.util.ArrayList
import java.util.Collection;
import java.util.Collections
import java.util.Comparator
import java.util.Date
import java.util.LinkedHashMap
import java.util.LinkedList
import java.util.List
import java.util.Map
import java.util.Set

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.SequenceGenerator
import javax.persistence.Transient

import org.apache.commons.beanutils.BeanComparator
import org.apache.commons.collections.comparators.ComparatorChain
import org.apache.commons.collections.comparators.ReverseComparator

import br.com.bluesoft.pronto.util.DateUtil

@Entity
@SequenceGenerator(name = "SEQ_SPRINT", sequenceName = "SEQ_SPRINT")
public class Sprint {

	@Id
	@GeneratedValue(generator = "SEQ_SPRINT")
	int sprintKey

	String nome

	Date dataInicial

	Date dataFinal

	boolean fechado

	boolean atual

	@Transient
	double esforcoTotal

	@Transient
	int valorDeNegocioTotal

	@OneToMany(mappedBy = "sprint")
	Set<Ticket> tickets

	String toString() {
		return getNome()
	}
	
	double getEsforcoRealizado(final Date date) {
		double total = 0
		if (tickets != null) {
			for (final Ticket ticket : tickets) {
				if ((ticket.isDefeito() || ticket.isEstoria()) && ticket.isSprintBacklog()) {
					if (DateUtil.toString(ticket.getDataDePronto()).equals(DateUtil.toString(date))) {
						total += ticket.getEsforco()
					}
				}
			}
		}
		return total
	}

	Map<String, Double> getMapaEsforcoPorDia() {
		final Map<String, Double> mapa = new LinkedHashMap<String, Double>()

		final List<Date> dias = getDias()
		for (final Date date : dias) {
			final String data = DateUtil.toStringMesAno(date)
			if (mapa.get(data) == null) {
				mapa.put(data, 0d)
			}
		}

		if (tickets != null) {
			for (final Ticket ticket : tickets) {

				if (ticket.getDataDePronto() == null) {
					continue
				}

				if ((ticket.isDefeito() || ticket.isEstoriaSemTarefa() || ticket.isTarefa()) && ticket.isSprintBacklog()) {
					final String data

					if (ticket.getDataDePronto().before(dataInicial)) {
						data = DateUtil.toStringMesAno(dataInicial)
					} else if (ticket.getDataDePronto().after(dataFinal)) {
						data = DateUtil.toStringMesAno(dataFinal)
					} else {
						data = DateUtil.toStringMesAno(ticket.getDataDePronto())
					}

					mapa.put(data, mapa.get(data) + ticket.getEsforco())
				}
			}
		}
		return mapa
	}

	void addTicket(final Ticket ticket) {
		tickets.add(ticket)
	}

	List<Ticket> getTicketsEmAberto() {
		final List<Ticket> ticketsEmAberto = new ArrayList<Ticket>()
		if (tickets != null) {
			for (final Ticket ticket : tickets) {
				if (!ticket.isDone() && ticket.isSprintBacklog()) {
					ticketsEmAberto.add(ticket)
				}
			}
		}
		return ticketsEmAberto
	}

	int getQuantidadeDeTickets() {
		return tickets != null ? tickets.size() : 0
	}

	List<Date> getDias() {

		final List<Date> dias = new LinkedList<Date>()

		Date atual = getDataInicial()
		while (atual.before(dataFinal)) {
			dias.add(atual)
			atual = DateUtil.add(atual, 1)
		}
		dias.add(dataFinal)
		return dias

	}

	List<Ticket> getTicketsParaOKanban() {
		final List<Ticket> ticketsParaOKanban = new ArrayList<Ticket>()
		if (tickets != null) {
			for (final Ticket ticket : tickets) {
				if (!ticket.temFilhos() && !ticket.isLixo()) {
					ticketsParaOKanban.add(ticket)
				}
			}
		}

		
		final List<Comparator> comparators = new ArrayList<Comparator>()

		final BeanComparator comparatorPrioridade = new BeanComparator("prioridade")
		final ReverseComparator comparatorValorDeNegocio = new ReverseComparator(new BeanComparator("valorDeNegocio"))
		final ReverseComparator comparatorEsforco = new ReverseComparator(new BeanComparator("esforco"))
		final BeanComparator comparatorDataDeCriacao = new BeanComparator("dataDeCriacao")
		final BeanComparator comparatorKey = new BeanComparator("ticketKey")

		comparators.add(comparatorPrioridade)
		comparators.add(comparatorValorDeNegocio)
		comparators.add(comparatorEsforco)
		comparators.add(comparatorDataDeCriacao)
		comparators.add(comparatorKey)

		final ComparatorChain comparatorChain = new ComparatorChain(comparators)
		Collections.sort(ticketsParaOKanban, comparatorChain)

		return ticketsParaOKanban

	}
	
	/**
	 * Retorna uma Mapa cujo Chave é o KanbanStatusKey e valor uma Collection com os Tickets Correspondentes 
	 */
	Map<Integer, Collection<Ticket>> getTicketsParaOKanbanPorEtapa() {
		def mapaPorEtapa = [:]
		def itens = this.getTicketsParaOKanban()
		itens.each() { Ticket it ->
			if (mapaPorEtapa[it.kanbanStatus.kanbanStatusKey] == null) {
				mapaPorEtapa[it.kanbanStatus.kanbanStatusKey] = [] as List
			}
			mapaPorEtapa[it.kanbanStatus.kanbanStatusKey].add(it)
		}
		return mapaPorEtapa
	}
	
	@Transient
	Integer getTempoDeVidaMedioEmDias() {
		
		def quantidade = 0
		def total = 0
		
		for (Ticket t: tickets) {
			if (!t.isTarefa()) {
				quantidade++
				total += t.getTempoDeVidaEmDias()
			}
		}
		
		return (quantidade > 0) ? (total / quantidade).setScale(2, RoundingMode.HALF_UP) : quantidade
	}
	
}
