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

import java.util.ArrayList
import java.util.Date
import java.util.List
import java.util.Set

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.OrderBy
import javax.persistence.SequenceGenerator

import org.hibernate.annotations.Cascade

import br.com.bluesoft.pronto.annotations.Label
import br.com.bluesoft.pronto.core.Backlog
import br.com.bluesoft.pronto.core.KanbanStatus
import br.com.bluesoft.pronto.core.TipoDeTicket
import br.com.bluesoft.pronto.service.Seguranca
import br.com.bluesoft.pronto.service.WikiFormatter

import com.google.common.collect.HashMultiset
import com.google.common.collect.Multiset

@Entity
@SequenceGenerator(name = "SEQ_TICKET", sequenceName = "SEQ_TICKET")
class Ticket {
	
	public static String BRANCH_MASTER = "master"
	
	public Ticket() {
		// Inicializando campos para binding do Spring.
		tipoDeTicket = new TipoDeTicket(TipoDeTicket.ESTORIA)
		backlog = new Backlog(Backlog.PRODUCT_BACKLOG)
		reporter = new Usuario()
		sprint = new Sprint()
		kanbanStatus = new KanbanStatus(KanbanStatus.TO_DO)
		comentarios = new ArrayList<TicketComentario>()
		logs = new ArrayList<TicketLog>()
		script = new Script()
	}
	
	@Id
	@GeneratedValue(generator = "SEQ_TICKET")
	int ticketKey
	
	@Label("título")
	String titulo
	
	@Label("backlog")
	@ManyToOne
	@JoinColumn(name = "BACKLOG_KEY")
	Backlog backlog
	
	@Label("status do kanban")
	@ManyToOne
	@JoinColumn(name = "KANBAN_STATUS_KEY")
	KanbanStatus kanbanStatus
	
	@ManyToOne
	@JoinColumn(name = "TIPO_DE_TICKET_KEY")
	@Label("tipo de ticket")
	TipoDeTicket tipoDeTicket
	
	@Label("descrição")
	String descricao
	
	@ManyToOne
	@JoinColumn(name = "REPORTER_KEY")
	Usuario reporter
	
	@Label("cliente")
	@ManyToOne
	@JoinColumn(name = "CLIENTE_KEY")
	Cliente cliente
	
	String solicitador
	
	String branch
	
	@Label("desenvolvedores")
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "TICKET_DESENVOLVEDOR", joinColumns =  @JoinColumn(name = "TICKET_KEY") , inverseJoinColumns =  @JoinColumn(name = "USUARIO_KEY") )
	Set<Usuario> desenvolvedores
	
	@Label("testadores")
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "TICKET_TESTADOR", joinColumns =  @JoinColumn(name = "TICKET_KEY") , inverseJoinColumns =  @JoinColumn(name = "USUARIO_KEY") )
	Set<Usuario> testadores
	
	@Label("valor de negócio")
	int valorDeNegocio
	
	@Label("esforço")
	double esforco
	
	@Label("em par?")
	boolean par
	
	@Label("planejado?")
	boolean planejado
	
	@ManyToOne
	@JoinColumn(name = "pai")
	@Cascade(org.hibernate.annotations.CascadeType.LOCK)
	Ticket pai
	
	@OneToMany(mappedBy = "pai", cascade = CascadeType.ALL)
	@OrderBy("prioridade asc")
	@Cascade(org.hibernate.annotations.CascadeType.LOCK)
	List<Ticket> filhos
	
	@Label("data de pronto")
	Date dataDePronto
	
	@Label("data de criaçao")
	Date dataDeCriacao
	
	@Label("prioridade de desenvolvimento")
	Integer prioridade
	
	@Label("prioridade do cliente")
	Integer prioridadeDoCliente
	
	@ManyToOne
	@JoinColumn(name = "sprint")
	Sprint sprint
	
	@OneToOne
	@JoinColumn(name = "script_key")
	Script script
	
	@OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
	final List<TicketLog> logs
	
	@OrderBy("data asc")
	@OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
	List<TicketComentario> comentarios
	
	String getTituloResumido() {
		
		if (titulo != null) {
			final int tamanhoMaximo = 45
			if (titulo.length() > tamanhoMaximo) {
				return titulo.substring(0, tamanhoMaximo) + "..."
			}
		}
		
		return titulo
	}
	
	String getHtml() {
		return WikiFormatter.toHtml(descricao)
	}
	
	double getSomaDoEsforcoDosFilhos() {
		if (temFilhos()) {
			double soma = 0d
			for (final Ticket filho : filhos) {
				soma += filho.getEsforco()
			}
			return soma
		}
		return 0d
	}
	
	double getEsforco() {
		if (temFilhos()) {
			return getSomaDoEsforcoDosFilhos()
		} else {
			return new BigDecimal(esforco).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue()
		}
	}
	
	void setEsforco(final double esforco) {
		this.esforco = new BigDecimal(esforco).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue()
	}
	
	Integer getPrioridade() {
		return prioridade != null ? prioridade : 0
	}
	
	@Override
	String toString() {
		return "#" + getTicketKey() + " - " + titulo
	}
	
	void addComentario(final String texto, final Usuario usuario) {
		final TicketComentario comentario = new TicketComentario()
		comentario.setTicket(this)
		comentario.setData(new Date())
		comentario.setUsuario(usuario)
		comentario.setTexto(texto)
		comentarios.add(comentario)
	}
	
	void addLogDeAlteracao(final String campo, final String valorAntigo, final String valorNovo) {
		final TicketLog log = new TicketLog()
		log.setTicket(this)
		log.setData(new Date())
		log.setUsuario(Seguranca.getUsuario().getUsername())
		log.setOperacao(TicketLog.ALTERACAO)
		log.setCampo(campo)
		log.setValorAntigo(valorAntigo)
		log.setValorNovo(valorNovo)
		logs.add(log)
	}
	
	
	boolean isSprintBacklog() {
		return getBacklog() != null && getBacklog().getBacklogKey() == Backlog.SPRINT_BACKLOG
	}
	
	void addDesenvolvedor(final Usuario usuario) {
		desenvolvedores.add(usuario)
	}
	
	void addTestador(final Usuario usuario) {
		testadores.add(usuario)
	}
	
	boolean isDefeito() {
		return getTipoDeTicket() != null && getTipoDeTicket().getTipoDeTicketKey() == TipoDeTicket.DEFEITO
	}
	
	boolean isEstoria() {
		return getTipoDeTicket() != null && getTipoDeTicket().getTipoDeTicketKey() == TipoDeTicket.ESTORIA
	}
	
	boolean isEstoriaSemTarefa() {
		return isEstoria() && !temFilhos()
	}
	
	boolean isTarefa() {
		return getTipoDeTicket() != null && getTipoDeTicket().getTipoDeTicketKey() == TipoDeTicket.TAREFA
	}
	
	boolean isDone() {
		return kanbanStatus != null && kanbanStatus.getKanbanStatusKey() == KanbanStatus.DONE
	}
	
	boolean isToDo() {
		return kanbanStatus != null && kanbanStatus.getKanbanStatusKey() == KanbanStatus.TO_DO
	}
	
	boolean isIdeia() {
		return getTipoDeTicket() != null && getTipoDeTicket().getTipoDeTicketKey() == TipoDeTicket.IDEIA
	}
	
	boolean temFilhos() {
		if (isTarefa()) {
			return false
		} else {
			return filhos != null && filhos.size() > 0
		}
	}
	
	boolean temPai() {
		if (!isTarefa()) {
			return false
		} else {
			return pai != null
		}
	}
	
	boolean isLixo() {
		return backlog != null && backlog.getBacklogKey() == Backlog.LIXEIRA
	}
	
	boolean isImpedido() {
		return backlog != null && backlog.getBacklogKey() == Backlog.IMPEDIMENTOS
	}
	
	Date getDataDeCriacao() {
		return dataDeCriacao
	}
	
	void setDataDeCriacao(final Date dataDeCriacao) {
		this.dataDeCriacao = dataDeCriacao
	}
	
	boolean isTodosOsFilhosProntos() {
		if (temFilhos()) {
			for (final Ticket filho : filhos) {
				if (!filho.isDone()) {
					return false
				}
			}
		}
		return true
	}
	
	String getDescricaoPar() {
		return isPar() ? "Par" : "Solo"
	}
	
	boolean isEmAndamento() {
		if (temFilhos()) {
			final Multiset<Integer> contadorDeStatus = HashMultiset.create()
			final int quantidadeDeFilhos = filhos.size()
			for (final Ticket filho : filhos) {
				contadorDeStatus.add(filho.getKanbanStatus().getKanbanStatusKey())
			}
			
			if (contadorDeStatus.count(KanbanStatus.DONE) != quantidadeDeFilhos && contadorDeStatus.count(KanbanStatus.TO_DO) != quantidadeDeFilhos) {
				return true
			} else {
				return false
			}
			
		} else {
			return !isDone() && !isToDo()
		}
		
	}
	
	Ticket getFilhoPorKey(final int filhoKey) {
		for (final Ticket f : filhos) {
			if (f.getTicketKey() == filhoKey) {
				return f
			}
		}
		return null
	}
	
	int getMaiorPrioridade() {
		int maiorPrioridade = 0
		for (final Ticket f : filhos) {
			maiorPrioridade = Math.max(maiorPrioridade, f.getPrioridade() == null ? 0 : f.getPrioridade())
		}
		return maiorPrioridade
	}
	
	int getMenorPrioridade() {
		int menorPrioridade = 0
		for (final Ticket f : filhos) {
			menorPrioridade = Math.min(menorPrioridade, f.getPrioridade() == null ? 0 : f.getPrioridade())
		}
		return menorPrioridade
	}
	
	List<Ticket> getFilhos() {
		return filhos;
	}
	
}
