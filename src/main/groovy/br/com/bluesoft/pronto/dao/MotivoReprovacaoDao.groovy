package br.com.bluesoft.pronto.dao;

import org.springframework.stereotype.Repository;

import br.com.bluesoft.pronto.model.MotivoReprovacao;

@Repository
public class MotivoReprovacaoDao extends DaoHibernate {

	MotivoReprovacaoDao() {
		super(MotivoReprovacao.class)
	}

	
}
