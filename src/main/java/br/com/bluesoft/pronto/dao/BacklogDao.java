package br.com.bluesoft.pronto.dao;

import org.springframework.stereotype.Repository;

import br.com.bluesoft.pronto.core.Backlog;

@Repository
public class BacklogDao extends DaoHibernate<Backlog, Integer> {

	public BacklogDao() {
		super(Backlog.class);
	}

}
