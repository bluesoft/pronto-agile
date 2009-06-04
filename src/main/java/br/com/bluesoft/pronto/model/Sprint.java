package br.com.bluesoft.pronto.model;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import br.com.bluesoft.pronto.util.DateUtil;

@Entity
public class Sprint {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int sprintKey;

	private String nome;

	private Date dataInicial;

	private Date dataFinal;

	private Blob imagem;

	private boolean fechado;

	private boolean atual;

	@OneToMany(mappedBy = "sprint")
	private List<Ticket> tickets;

	public int getSprintKey() {
		return sprintKey;
	}

	public void setSprintKey(final int sprintKey) {
		this.sprintKey = sprintKey;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(final String nome) {
		this.nome = nome;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(final Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(final Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Blob getImagem() {
		return imagem;
	}

	public void setImagem(final Blob imagem) {
		this.imagem = imagem;
	}

	@Override
	public String toString() {
		return getNome();
	}

	public boolean isFechado() {
		return fechado;
	}

	public void setFechado(final boolean fechado) {
		this.fechado = fechado;
	}

	public boolean isAtual() {
		return atual;
	}

	public void setAtual(final boolean atual) {
		this.atual = atual;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public int getEsforcoTotal() {
		int total = 0;
		for (final Ticket ticket : tickets) {
			if (ticket.isDefeito() || ticket.isEstoria()) {
				total += ticket.getEsforco();
			}
		}
		return total;
	}

	public int getValorDeNegocioTotal() {
		int total = 0;
		for (final Ticket ticket : tickets) {
			if (ticket.isDefeito() || ticket.isEstoria()) {
				total += ticket.getValorDeNegocio();
			}
		}
		return total;
	}

	public int getEsforcoRealizado(final Date date) {
		int total = 0;
		for (final Ticket ticket : tickets) {
			if (ticket.isDefeito() || ticket.isEstoria()) {
				if (DateUtil.toString(ticket.getDataDePronto()).equals(DateUtil.toString(date))) {
					total += ticket.getEsforco();
				}
			}
		}
		return total;
	}

	public void addTicket(final Ticket ticket) {
		tickets.add(ticket);
	}

	public List<Ticket> getTicketsEmAberto() {
		final List<Ticket> ticketsEmAberto = new ArrayList<Ticket>();
		for (final Ticket ticket : tickets) {
			if (!ticket.isDone()) {
				ticketsEmAberto.add(ticket);
			}
		}
		return ticketsEmAberto;
	}

	public List<Date> getDias() {

		final List<Date> dias = new LinkedList<Date>();

		Date atual = getDataInicial();
		while (atual.before(dataFinal)) {
			dias.add(atual);
			atual = DateUtil.add(atual, 1);
		}

		return dias;

	}
}
