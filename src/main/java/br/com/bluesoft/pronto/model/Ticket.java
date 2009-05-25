package br.com.bluesoft.pronto.model;

import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import br.com.bluesoft.pronto.core.TipoDeTicket;

@Entity
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int ticketKey;

	private String titulo;

	@ManyToOne
	@JoinColumn(name = "TIPO_DE_TICKET")
	private TipoDeTicket tipoDeTicket;

	private String descricao;

	@ManyToOne
	@JoinColumn(name = "REPORTER_KEY")
	private Usuario reporter;

	private String cliente;

	private String solicitador;

	@ManyToMany
	@JoinTable(name = "TICKET_DESENVOLVEDOR", joinColumns = { @JoinColumn(name = "TICKET_KEY") }, inverseJoinColumns = { @JoinColumn(name = "USUARIO_KEY") })
	private Set<Usuario> desenvolvedores;

	private int valorDeNegocio;

	private int esforco;

	private boolean par;

	@ManyToOne
	@JoinColumn(name = "pai")
	private Ticket ticket;

	@OneToMany(mappedBy = "ticket")
	private List<Ticket> filhos;

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

	public Ticket getTicket() {
		return ticket;
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

	public void setTicket(final Ticket ticket) {
		this.ticket = ticket;
	}

	public void setFilhos(final List<Ticket> filhos) {
		this.filhos = filhos;
	}

	public TipoDeTicket getTipoDeTicket() {
		return tipoDeTicket;
	}

	public void setTipoDeTicket(TipoDeTicket tipoDeTicket) {
		this.tipoDeTicket = tipoDeTicket;
	}

}
