package br.com.bluesoft.pronto.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import br.com.bluesoft.pronto.util.DateUtil;

@Entity
@SequenceGenerator(name = "SEQ_SPRINT", sequenceName = "SEQ_SPRINT")
public class Sprint {

	@Id
	@GeneratedValue(generator = "SEQ_SPRINT")
	private int sprintKey;

	private String nome;

	private Date dataInicial;

	private Date dataFinal;

	private boolean fechado;

	private boolean atual;

	@OneToMany(mappedBy = "sprint")
	private Set<Ticket> tickets;

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

	public Set<Ticket> getTickets() {
		return tickets;
	}

	public int getEsforcoTotal() {
		int total = 0;
		if (tickets != null) {
			for (final Ticket ticket : tickets) {
				if (ticket.isDefeito() || ticket.isEstoria()) {
					total += ticket.getEsforco();
				}
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

	public Map<String, Double> getMapaEsforcoPorDia() {
		final Map<String, Double> mapa = new LinkedHashMap<String, Double>();

		final List<Date> dias = getDias();
		for (final Date date : dias) {
			final String data = DateUtil.toStringMesAno(date);
			if (mapa.get(data) == null) {
				mapa.put(data, 0d);
			}
		}

		for (final Ticket ticket : tickets) {

			if (ticket.getDataDePronto() == null) {
				continue;
			}

			if (ticket.isDefeito() || ticket.isEstoria()) {
				final String data;
				if (ticket.getDataDePronto().after(dataFinal)) {
					data = DateUtil.toStringMesAno(dataFinal);
				} else {
					data = DateUtil.toStringMesAno(ticket.getDataDePronto());
				}

				mapa.put(data, mapa.get(data) + ticket.getEsforco());
			}
		}
		return mapa;
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

	public int getQuantidadeDeTickets() {
		return tickets != null ? tickets.size() : 0;
	}

	public List<Date> getDias() {

		final List<Date> dias = new LinkedList<Date>();

		Date atual = getDataInicial();
		while (atual.before(dataFinal)) {
			dias.add(atual);
			atual = DateUtil.add(atual, 1);
		}
		dias.add(dataFinal);
		return dias;

	}
}
