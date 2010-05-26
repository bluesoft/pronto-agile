package br.com.bluesoft.pronto.dao;

import org.springframework.stereotype.Repository;

import br.com.bluesoft.pronto.model.MovimentoKanban;

@Repository
public class MovimentoKanbanDao extends DaoHibernate {

	MovimentoKanbanDao() {
		super(MovimentoKanban.class)
	}
	
}
