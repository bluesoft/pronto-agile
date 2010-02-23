package br.com.bluesoft.pronto.dao;

import org.springframework.stereotype.Repository;

import br.com.bluesoft.pronto.model.Categoria;

@Repository 
class CategoriaDao extends DaoHibernate {
	
	CategoriaDao() {
		super(Categoria.class)
	}
	
}
