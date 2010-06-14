package br.com.bluesoft.pronto.dao;

import org.springframework.stereotype.Repository;

import br.com.bluesoft.pronto.model.MovimentoKanban;

@Repository
public class MovimentoKanbanDao extends DaoHibernate {

	MovimentoKanbanDao() {
		super(MovimentoKanban.class)
	}
	
	List<MovimentoKanban> listarMovimentosDoTicket(int ticketKey){
		buscar "from MovimentoKanban mk inner join fetch mk.kanbanStatus where mk.ticket.ticketKey = ? order by mk.data", ticketKey
	}
	
	List<MovimentoKanban> listarUltimosMovimentos(){
		def hql = "from MovimentoKanban mk inner join fetch mk.kanbanStatus order by mk.data"
		return getSession().createQuery(hql).setMaxResults(10).list()
	}
}
