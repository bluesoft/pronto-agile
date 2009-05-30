package br.com.bluesoft.pronto.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import br.com.bluesoft.pronto.core.Backlog;
import br.com.bluesoft.pronto.core.KanbanStatus;
import br.com.bluesoft.pronto.core.TipoDeTicket;
import br.com.bluesoft.pronto.service.WikiFormatter;

@Entity
public class Ticket {

	public Ticket() {
		super();
		tipoDeTicket = new TipoDeTicket(TipoDeTicket.ESTORIA);
		backlog = new Backlog(Backlog.PRODUCT_BACKLOG);
		reporter = new Usuario();
		sprint = new Sprint();
		kanbanStatus = new KanbanStatus(KanbanStatus.TO_DO);

		this.comentarios = new ArrayList<TicketComentario>();
		this.logs = new ArrayList<TicketLog>();

	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int ticketKey;

	@Label("título")
	private String titulo;

	@ManyToOne
	@JoinColumn(name = "BACKLOG_KEY")
	private Backlog backlog;

	@ManyToOne
	@JoinColumn(name = "KANBAN_STATUS_KEY")
	private KanbanStatus kanbanStatus;

	@Label("tipo de ticket")
	@ManyToOne
	@JoinColumn(name = "TIPO_DE_TICKET_KEY")
	private TipoDeTicket tipoDeTicket;

	@Label("descrição")
	private String descricao;

	@ManyToOne
	@JoinColumn(name = "REPORTER_KEY")
	private Usuario reporter;

	private String cliente;

	private String solicitador;

	private String branch;

	@ManyToMany
	@JoinTable(name = "TICKET_DESENVOLVEDOR", joinColumns = { @JoinColumn(name = "TICKET_KEY") }, inverseJoinColumns = { @JoinColumn(name = "USUARIO_KEY") })
	private Set<Usuario> desenvolvedores;

	@Label("valor de negócio")
	private int valorDeNegocio;

	@Label("esforço")
	private int esforco;

	@Label("em par?")
	private boolean par;

	@Label("planejado?")
	private boolean planejado;

	@ManyToOne
	@JoinColumn(name = "pai")
	private Ticket pai;

	@OneToMany(mappedBy = "pai")
	private List<Ticket> filhos;

	private Date dataDePronto;

	@ManyToOne
	@JoinColumn(name = "sprint")
	private Sprint sprint;

	@OneToMany(mappedBy = "ticket")
	private List<TicketLog> logs;

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

	public int getValorDeNegocio() {
		return valorDeNegocio;
	}

	public void setValorDeNegocio(final int valorDeNegocio) {
		this.valorDeNegocio = valorDeNegocio;
	}

	public int getEsforco() {
		return esforco;
	}

	public void setEsforco(final int esforco) {
		this.esforco = esforco;
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

	public void setPai(Ticket pai) {
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

	public void setBacklog(Backlog backlog) {
		this.backlog = backlog;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public boolean isPlanejado() {
		return planejado;
	}

	public void setPlanejado(boolean planejado) {
		this.planejado = planejado;
	}

	public List<TicketLog> getLogs() {
		return logs;
	}

	public List<TicketComentario> getComentarios() {
		return comentarios;
	}

	public void setComentarios(List<TicketComentario> comentarios) {
		this.comentarios = comentarios;
	}

	@Override
	public String toString() {
		return "#" + this.getTicketKey();
	}

	public void addComentario(String texto, String usuario) {
		TicketComentario comentario = new TicketComentario();
		comentario.setTicket(this);
		comentario.setData(new Date());
		comentario.setUsuario(usuario);
		comentario.setTexto(texto);
		this.comentarios.add(comentario);
	}

	public KanbanStatus getKanbanStatus() {
		return kanbanStatus;
	}

	public void setKanbanStatus(KanbanStatus kanbanStatus) {
		this.kanbanStatus = kanbanStatus;
	}

	public boolean isSprintBacklog() {
		return this.getBacklog() != null && this.getBacklog().getBacklogKey() == Backlog.SPRINT_BACKLOG;
	}

	public void addDesenvolvedor(Usuario usuario) {
		this.desenvolvedores.add(usuario);
	}

	public Date getDataDePronto() {
		return dataDePronto;
	}

	public void setDataDePronto(Date dataDePronto) {
		this.dataDePronto = dataDePronto;
	}

	public boolean isDefeito() {
		return this.getTipoDeTicket().getTipoDeTicketKey() == TipoDeTicket.DEFEITO;
	}

	public boolean isEstoria() {
		return this.getTipoDeTicket().getTipoDeTicketKey() == TipoDeTicket.ESTORIA;
		return false;
	}

}
