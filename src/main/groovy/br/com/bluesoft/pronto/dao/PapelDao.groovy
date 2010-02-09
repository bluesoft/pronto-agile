package br.com.bluesoft.pronto.dao

import java.util.List

import org.hibernate.criterion.Order
import org.springframework.stereotype.Repository

import br.com.bluesoft.pronto.core.Papel

@Repository
public class PapelDao extends DaoHibernate {

	PapelDao() {
		super(Papel.class)
	}

	List<Papel> listar() {
		return getSession().createCriteria(Papel.class).addOrder(Order.asc("papelKey")).list()
	}
}
