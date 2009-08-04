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
	@GeneratedValue(generator = "SEQ_TICKET_COMENTARIO")
	private int ticketComentarioKey;

	@ManyToOne
	@JoinColumn(name = "USUARIO_KEY")
	private Usuario usuario;

	private Date data;

	private String texto;

	@ManyToOne
	@JoinColumn(name = "TICKET_KEY")
	private Ticket ticket;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(final Usuario usuario) {
		this.usuario = usuario;
	}

	public int getTicketComentarioKey() {
		return ticketComentarioKey;
	}

	public void setTicketComentarioKey(final int ticketComentarioKey) {
		this.ticketComentarioKey = ticketComentarioKey;
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
