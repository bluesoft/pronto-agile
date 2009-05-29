package br.com.bluesoft.pronto.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.bluesoft.pronto.service.WikiFormatter;

@Entity
public class TicketComentario {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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

	public void setTicketComentarioKey(int ticketComentarioKey) {
		this.ticketComentarioKey = ticketComentarioKey;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
	
	public String getHtml() {
		return WikiFormatter.toHtml(this.getTexto());
	}
}
