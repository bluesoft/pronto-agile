package br.com.bluesoft.pronto.dao

import java.util.List

import org.hibernate.criterion.Order
import org.hibernate.criterion.Restrictions
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
	
	
	List<KanbanStatus> listarPorProjeto(int projetoKey) {
		return getSession().createCriteria(KanbanStatus.class)
		.add(Restrictions.eq('projeto.projetoKey',projetoKey))
		.addOrder(Order.asc("ordem")).list()
	}
	

	KanbanStatus obterInicioPorProjeto(int projetoKey) {
		return (KanbanStatus) session.createCriteria(KanbanStatus.class)
					  .add(Restrictions.eq('projeto.projetoKey',projetoKey))
					  .add(Restrictions.eq('inicio',true))
					  .uniqueResult()
	}
	
	KanbanStatus obterFimPorProjeto(int projetoKey) {
		return (KanbanStatus) session.createCriteria(KanbanStatus.class)
					  .add(Restrictions.eq('projeto.projetoKey',projetoKey))
					  .add(Restrictions.eq('fim',true))
					  .uniqueResult()
	}
		
}


