package br.com.bluesoft.pronto.model;

import java.text.MessageFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class TicketLog {

	public static final int INCLUSAO = 1;
	public static final int ALTERACAO = 2;
	public static final int EXCLUSAO = 3;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int ticketHistoryKey;

	@ManyToOne
	@JoinColumn(name = "TICKET_KEY")
	private Ticket ticket;

	private String campo;

	private int operacao;

	private String valorAntigo;

	private String valorNovo;

	private Date data;

	private String usuario;

	public int getTicketHistoryKey() {
		return ticketHistoryKey;
	}

	public void setTicketHistoryKey(int ticketHistoryKey) {
		this.ticketHistoryKey = ticketHistoryKey;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public int getOperacao() {
		return operacao;
	}

	public void setOperacao(int operacao) {
		this.operacao = operacao;
	}

	public String getValorAntigo() {
		return valorAntigo;
	}

	public void setValorAntigo(String valorAntigo) {
		this.valorAntigo = valorAntigo;
	}

	public String getValorNovo() {
		return valorNovo;
	}

	public void setValorNovo(String valorNovo) {
		this.valorNovo = valorNovo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getDescricao() {

		if (this.getOperacao() == ALTERACAO) {
			return MessageFormat.format("{4} - {3} - {0} mudou de {1} para {2}", campo, valorAntigo, valorNovo, usuario, data);
		} else
			return null;
	}
}
