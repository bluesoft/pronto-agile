package br.com.bluesoft.pronto.dao;

import org.springframework.stereotype.Repository;

import br.com.bluesoft.pronto.model.Ticket;

@Repository
public class TicketDao extends DaoHibernate<Ticket, Integer> {

	public TicketDao() {
		super(Ticket.class);
	}

}
