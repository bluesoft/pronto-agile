/*
 * Copyright 2009 Pronto Agile Project Management.
 *
 * This file is part of Pronto.
 *
 * Pronto is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Pronto is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Pronto. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package br.com.bluesoft.pronto.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;

import br.com.bluesoft.pronto.annotations.Label;
import br.com.bluesoft.pronto.core.Backlog;
import br.com.bluesoft.pronto.core.KanbanStatus;
import br.com.bluesoft.pronto.core.TipoDeTicket;
import br.com.bluesoft.pronto.service.Seguranca;
import br.com.bluesoft.pronto.service.WikiFormatter;

@Entity
@SequenceGenerator(name = "SEQ_TICKET", sequenceName = "SEQ_TICKET")
public class Ticket {

	public static String BRANCH_MASTER = "master";

	public Ticket() {
		// Inicializando campos para binding do Spring.
		super();
		tipoDeTicket = new TipoDeTicket(TipoDeTicket.ESTORIA);
		backlog = new Backlog(Backlog.PRODUCT_BACKLOG);
		reporter = new Usuario();
		sprint = new Sprint();
		kanbanStatus = new KanbanStatus(KanbanStatus.TO_DO);
		comentarios = new ArrayList<TicketComentario>();
		logs = new ArrayList<TicketLog>();
	}

	@Id
	@GeneratedValue(generator = "SEQ_TICKET")
	private int ticketKey;

	@Label("título")
	private String titulo;

	@Label("backlog")
	@ManyToOne
	@JoinColumn(name = "BACKLOG_KEY")
	private Backlog backlog;

	@Label("status do kanban")
	@ManyToOne
	@JoinColumn(name = "KANBAN_STATUS_KEY")
	private KanbanStatus kanbanStatus;

	@ManyToOne
	@JoinColumn(name = "TIPO_DE_TICKET_KEY")
	@Label("tipo de ticket")
	private TipoDeTicket tipoDeTicket;

	@Label("descrição")
	private String descricao;

	@ManyToOne
	@JoinColumn(name = "REPORTER_KEY")
	private Usuario reporter;

	private String cliente;

	private String solicitador;

	private String branch;

	@Label("desenvolvedores")
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "TICKET_DESENVOLVEDOR", joinColumns = { @JoinColumn(name = "TICKET_KEY") }, inverseJoinColumns = { @JoinColumn(name = "USUARIO_KEY") })
	private Set<Usuario> desenvolvedores;

	@Label("testadores")
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "TICKET_TESTADOR", joinColumns = { @JoinColumn(name = "TICKET_KEY") }, inverseJoinColumns = { @JoinColumn(name = "USUARIO_KEY") })
	private Set<Usuario> testadores;

	@Label("valor de negócio")
	private int valorDeNegocio;

	@Label("esforço")
	private double esforco;

	@Label("em par?")
	private boolean par;

	@Label("planejado?")
	private boolean planejado;

	@ManyToOne
	@JoinColumn(name = "pai")
	private Ticket pai;

	@OneToMany(mappedBy = "pai", cascade = CascadeType.ALL)
	@OrderBy("esforco desc, ticketKey asc")
	private List<Ticket> filhos;

	@Label("data de pronto")
	private Date dataDePronto;

	@Label("data de criaçao")
	private Date dataDeCriacao;

	@ManyToOne
	@JoinColumn(name = "sprint")
	private Sprint sprint;

	@OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
	private final List<TicketLog> logs;

	@OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
	private List<TicketComentario> comentarios;

	public int getTicketKey() {
		return ticketKey;
	}

	public void setTicketKey(final int ticketKey) {
		this.ticketKey = ticketKey;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getTituloResumido() {

		if (titulo != null) {
			final int tamanhoMaximo = 45;
			if (titulo.length() > tamanhoMaximo) {
				return titulo.substring(0, tamanhoMaximo) + "...";
			}
		}

		return titulo;
	}

	public void setTitulo(final String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getHtml() {
		return WikiFormatter.toHtml(descricao);
	}

	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

	public List<Ticket> getFilhos() {
		return filhos;
	}

	public Usuario getReporter() {
		return reporter;
	}

	public void setReporter(final Usuario reporter) {
		this.reporter = reporter;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(final String cliente) {
		this.cliente = cliente;
	}

	public String getSolicitador() {
		return solicitador;
	}

	public void setSolicitador(final String solicitador) {
		this.solicitador = solicitador;
	}

	public Set<Usuario> getDesenvolvedores() {
		return desenvolvedores;
	}

	public void setDesenvolvedores(final Set<Usuario> desenvolvedores) {
		this.desenvolvedores = desenvolvedores;
	}

	public Set<Usuario> getTestadores() {
		return testadores;
	}

	public void setTestadores(final Set<Usuario> testadores) {
		this.testadores = testadores;
	}

	public int getValorDeNegocio() {
		return valorDeNegocio;
	}

	public void setValorDeNegocio(final int valorDeNegocio) {
		this.valorDeNegocio = valorDeNegocio;
	}

	public double getSomaDoEsforcoDosFilhos() {
		if (temFilhos()) {
			double soma = 0d;
			for (final Ticket filho : filhos) {
				soma += filho.getEsforco();
			}
			return soma;
		}
		return 0d;
	}

	public double getEsforco() {
		if (temFilhos()) {
			return getSomaDoEsforcoDosFilhos();
		} else {
			return new BigDecimal(esforco).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
	}

	public void setEsforco(final double esforco) {
		this.esforco = new BigDecimal(esforco).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public boolean isPar() {
		return par;
	}

	public void setPar(final boolean par) {
		this.par = par;
	}

	public Ticket getPai() {
		return pai;
	}

	public void setPai(final Ticket pai) {
		this.pai = pai;
	}

	public void setFilhos(final List<Ticket> filhos) {
		this.filhos = filhos;
	}

	public TipoDeTicket getTipoDeTicket() {
		return tipoDeTicket;
	}

	public void setTipoDeTicket(final TipoDeTicket tipoDeTicket) {
		this.tipoDeTicket = tipoDeTicket;
	}

	public Sprint getSprint() {
		return sprint;
	}

	public void setSprint(final Sprint sprint) {
		this.sprint = sprint;
	}

	public Backlog getBacklog() {
		return backlog;
	}

	public void setBacklog(final Backlog backlog) {
		this.backlog = backlog;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(final String branch) {
		this.branch = branch;
	}

	public boolean isPlanejado() {
		return planejado;
	}

	public void setPlanejado(final boolean planejado) {
		this.planejado = planejado;
	}

	public List<TicketLog> getLogs() {
		return logs;
	}

	public List<TicketComentario> getComentarios() {
		return comentarios;
	}

	public void setComentarios(final List<TicketComentario> comentarios) {
		this.comentarios = comentarios;
	}

	@Override
	public String toString() {
		return "#" + getTicketKey() + " - " + titulo;
	}

	public void addComentario(final String texto, final Usuario usuario) {
		final TicketComentario comentario = new TicketComentario();
		comentario.setTicket(this);
		comentario.setData(new Date());
		comentario.setUsuario(usuario);
		comentario.setTexto(texto);
		comentarios.add(comentario);
	}

	public void addLogDeAlteracao(final String campo, final String valorAntigo, final String valorNovo) {
		final TicketLog log = new TicketLog();
		log.setTicket(this);
		log.setData(new Date());
		log.setUsuario(Seguranca.getUsuario().getUsername());
		log.setOperacao(TicketLog.ALTERACAO);
		log.setCampo(campo);
		log.setValorAntigo(valorAntigo);
		log.setValorNovo(valorNovo);
		logs.add(log);
	}

	public KanbanStatus getKanbanStatus() {
		return kanbanStatus;
	}

	public void setKanbanStatus(final KanbanStatus kanbanStatus) {
		this.kanbanStatus = kanbanStatus;
	}

	public boolean isSprintBacklog() {
		return getBacklog() != null && getBacklog().getBacklogKey() == Backlog.SPRINT_BACKLOG;
	}

	public void addDesenvolvedor(final Usuario usuario) {
		desenvolvedores.add(usuario);
	}

	public void addTestador(final Usuario usuario) {
		testadores.add(usuario);
	}

	public Date getDataDePronto() {
		return dataDePronto;
	}

	public void setDataDePronto(final Date dataDePronto) {
		this.dataDePronto = dataDePronto;
	}

	public boolean isDefeito() {
		return getTipoDeTicket() != null && getTipoDeTicket().getTipoDeTicketKey() == TipoDeTicket.DEFEITO;
	}

	public boolean isEstoria() {
		return getTipoDeTicket() != null && getTipoDeTicket().getTipoDeTicketKey() == TipoDeTicket.ESTORIA;
	}

	public boolean isEstoriaSemTarega() {
		return isEstoria() && !temFilhos();
	}

	public boolean isTarefa() {
		return getTipoDeTicket() != null && getTipoDeTicket().getTipoDeTicketKey() == TipoDeTicket.TAREFA;
	}

	public boolean isDone() {
		return kanbanStatus != null && kanbanStatus.getKanbanStatusKey() == KanbanStatus.DONE;
	}

	public boolean isIdeia() {
		return getTipoDeTicket() != null && getTipoDeTicket().getTipoDeTicketKey() == TipoDeTicket.IDEIA;
	}

	public boolean temFilhos() {
		if (isTarefa()) {
			return false;
		} else {
			return filhos != null && filhos.size() > 0;
		}
	}

	public boolean temPai() {
		if (!isTarefa()) {
			return false;
		} else {
			return pai != null;
		}
	}

	public boolean isLixo() {
		return backlog != null && backlog.getBacklogKey() == Backlog.LIXEIRA;
	}

	public boolean isImpedido() {
		return backlog != null && backlog.getBacklogKey() == Backlog.IMPEDIMENTOS;
	}

	public Date getDataDeCriacao() {
		return dataDeCriacao;
	}

	public void setDataDeCriacao(final Date dataDeCriacao) {
		this.dataDeCriacao = dataDeCriacao;
	}

	public boolean isTodosOsFilhosProntos() {
		if (temFilhos()) {
			for (final Ticket filho : filhos) {
				if (!filho.isDone()) {
					return false;
				}
			}
		}
		return true;
	}

	public String getDescricaoPar() {
		return isPar() ? "Par" : "Solo";
	}

}
