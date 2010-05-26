package br.com.bluesoft.pronto.dao;

import org.springframework.stereotype.Repository;

import br.com.bluesoft.pronto.model.Categoria;
import br.com.bluesoft.pronto.model.CausaDeDefeito;

@Repository 
class CausaDeDefeitoDao extends DaoHibernate {
	
	CausaDeDefeitoDao() {
		super(CausaDeDefeito.class)
	}
	
}
