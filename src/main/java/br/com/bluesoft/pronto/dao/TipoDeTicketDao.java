package br.com.bluesoft.pronto.dao;

import org.springframework.stereotype.Repository;

import br.com.bluesoft.pronto.core.TipoDeTicket;

@Repository
public class TipoDeTicketDao extends DaoHibernate<TipoDeTicket, Integer> {

	public TipoDeTicketDao() {
		super(TipoDeTicket.class);
	}

}
