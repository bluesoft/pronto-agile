package br.com.bluesoft.pronto.dao;

import java.util.List;

import org.hibernate.Criteria
import org.hibernate.criterion.Order
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.bluesoft.pronto.model.Categoria;
import br.com.bluesoft.pronto.model.Sprint;

@Repository 
class CategoriaDao extends DaoHibernate {

	@Autowired
	private SessionFactory sessionFactory
		
	CategoriaDao() {
		super(Categoria.class)
	}

	@Override
	public List<Categoria> listar() {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Categoria.class)
		criteria.addOrder(Order.asc("descricao"))
		return criteria.list()
	}
}
