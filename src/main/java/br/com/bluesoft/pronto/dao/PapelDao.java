package br.com.bluesoft.pronto.dao;

import org.springframework.stereotype.Repository;

import br.com.bluesoft.pronto.core.Papel;

@Repository
public class PapelDao extends DaoHibernate<Papel, Integer> {

	public PapelDao() {
		super(Papel.class);
	}
}
