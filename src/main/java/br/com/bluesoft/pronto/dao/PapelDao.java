package br.com.bluesoft.pronto.dao;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import br.com.bluesoft.pronto.core.Papel;

@Repository
public class PapelDao extends DaoHibernate<Papel, Integer> {

	public PapelDao() {
		super(Papel.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Papel> listar() {
		return getSession().createCriteria(Papel.class).addOrder(Order.asc("papelKey")).list();
	}
}
