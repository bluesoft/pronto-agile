package br.com.bluesoft.pronto.dao

import java.util.List

import org.springframework.stereotype.Repository

import br.com.bluesoft.pronto.model.BancoDeDados

@Repository
public class BancoDeDadosDao extends DaoHibernate {

	BancoDeDadosDao() {
		super(BancoDeDados.class)
	}

	@Override
	List listar() {
		return getSession().createQuery("from BancoDeDados b order by b.nome").list()
	}

}
