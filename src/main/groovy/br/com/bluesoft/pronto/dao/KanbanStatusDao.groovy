package br.com.bluesoft.pronto.dao

import java.util.List

import org.hibernate.criterion.Order
import org.springframework.stereotype.Repository

import br.com.bluesoft.pronto.core.KanbanStatus

@Repository
class KanbanStatusDao extends DaoHibernate {
	
	KanbanStatusDao() {
		super(KanbanStatus.class)
	}
	
	List<KanbanStatus> listar() {
		return getSession().createCriteria(KanbanStatus.class).addOrder(Order.asc("ordem")).list()
	}
	
}
