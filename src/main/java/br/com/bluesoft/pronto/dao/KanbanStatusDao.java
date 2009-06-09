package br.com.bluesoft.pronto.dao;

import org.springframework.stereotype.Repository;

import br.com.bluesoft.pronto.core.KanbanStatus;

@Repository
public class KanbanStatusDao extends DaoHibernate<KanbanStatus, Integer> {

	public KanbanStatusDao() {
		super(KanbanStatus.class);
	}
}
