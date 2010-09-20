package br.com.bluesoft.pronto.dao;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import br.com.bluesoft.pronto.model.Categoria;
import br.com.bluesoft.pronto.model.Modulo;

@Repository 
class ModuloDao extends DaoHibernate {
	
	ModuloDao() {
		super(Modulo.class)
	}
	
	@Override
	public List listar() {
		return this.getSession().createCriteria(clazz).addOrder(Order.asc("descricao")).list()
	}
	
}
