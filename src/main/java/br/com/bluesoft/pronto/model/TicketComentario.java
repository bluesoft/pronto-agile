package br.com.bluesoft.pronto.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.bluesoft.pronto.service.WikiFormatter;

@Entity
@SequenceGenerator(name = "SEQ_TICKET_COMENTARIO", sequenceName = "SEQ_TICKET_COMENTARIO")
public class TicketComentario {

	@Id
	@GeneratedValue(generator = "")
	private int ticketComentarioKey;

	private String usuario;

	private Date data;

	private String texto;

	@ManyToOne
	@JoinColumn(name = "TICKET_KEY")
	private Ticket ticket;

	public int getTicketComentarioKey() {
		return ticketComentarioKey;
	}

	public void setTicketComentarioKey(final int ticketComentarioKey) {
		this.ticketComentarioKey = ticketComentarioKey;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(final String usuario) {
		this.usuario = usuario;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(final String texto) {
		this.texto = texto;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(final Ticket ticket) {
		this.ticket = ticket;
	}

	public Date getData() {
		return data;
	}

	public void setData(final Date data) {
		this.data = data;
	}

	public String getHtml() {
		return WikiFormatter.toHtml(getTexto());
	}
}
