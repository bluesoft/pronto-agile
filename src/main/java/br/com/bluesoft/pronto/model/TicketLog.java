package br.com.bluesoft.pronto.model;

import java.text.MessageFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.bluesoft.pronto.service.WikiFormatter;

@Entity
@SequenceGenerator(name = "SEQ_TICKET_LOG", sequenceName = "SEQ_TICKET_LOG")
public class TicketLog {

	public static final int INCLUSAO = 1;
	public static final int ALTERACAO = 2;
	public static final int EXCLUSAO = 3;

	public static final String EM_BRANCO = "Em Branco";

	@Id
	@GeneratedValue(generator = "SEQ_TICKET_LOG")
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

	public void setTicketHistoryKey(final int ticketHistoryKey) {
		this.ticketHistoryKey = ticketHistoryKey;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(final Ticket ticket) {
		this.ticket = ticket;
	}

	public String getCampo() {
		return campo;
	}

	public void setCampo(final String campo) {
		this.campo = campo;
	}

	public int getOperacao() {
		return operacao;
	}

	public void setOperacao(final int operacao) {
		this.operacao = operacao;
	}

	public String getValorAntigo() {
		return valorAntigo == null || valorAntigo.equals("null") ? "Em Branco" : valorAntigo;
	}

	public void setValorAntigo(final String valorAntigo) {
		this.valorAntigo = trataValor(valorAntigo);

	}

	public String getValorNovo() {
		return valorNovo == null || valorNovo.equals("null") ? "Em Branco" : valorNovo;
	}

	public void setValorNovo(final String valorNovo) {
		this.valorNovo = trataValor(valorNovo);
	}

	public Date getData() {
		return data;
	}

	public void setData(final Date data) {
		this.data = data;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(final String usuario) {
		this.usuario = usuario;
	}

	public String getDescricao() {

		if (getOperacao() == ALTERACAO) {
			return MessageFormat.format("{0} - {1} - {2} mudou de \"{3}\" para \"{4}\"", data, usuario, campo, valorAntigo, valorNovo);
		} else {
			return MessageFormat.format("{0} - {1} - {2} foi definido como \"{3}\"", data, usuario, campo, valorNovo);
		}
	}

	private String trataValor(final String in) {
		if (in == null || in.equals("null") || in.length() <= 0) {
			return null;
		} else {
			if (in.equals("true")) {
				return "sim";
			} else if (in.equals("false")) {
				return "não";
			} else {
				return in;
			}
		}
	}

	public boolean isDiferente() {

		if (getValorAntigo() == null && getValorNovo() == null) {
			return false;
		}

		if (getValorAntigo() != null && getValorNovo() == null) {
			return true;
		}

		if (getValorAntigo() == null && getValorNovo() != null) {
			return true;
		}

		if (getValorAntigo() != null && getValorNovo() != null) {
			return !getValorAntigo().equals(getValorNovo());
		}

		return true;

	}

	public String getValorAntigoHtml() {
		return WikiFormatter.toHtml(valorAntigo);
	}

	public String getValorNovoHtml() {
		return WikiFormatter.toHtml(valorNovo);
	}

}
