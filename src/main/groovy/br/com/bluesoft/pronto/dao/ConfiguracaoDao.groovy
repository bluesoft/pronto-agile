package br.com.bluesoft.pronto.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.bluesoft.pronto.model.Configuracao;

class ConfiguracaoDao extends DaoHibernate{
	
	ConfiguracaoDao() {
		super(Configuracao.class)
	}
	
}
