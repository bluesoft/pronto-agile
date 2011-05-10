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
		listarUltimosMovimentos(10)
	}
	
	List<MovimentoKanban> listarUltimosMovimentos(int quantos){
		return listarUltimosMovimentos(quantos, null)
	}
	
	List<MovimentoKanban> listarUltimosMovimentos(int quantos, String username){
		def hql = "from MovimentoKanban mk inner join fetch mk.kanbanStatus"
		if (username) {
			hql += " where mk.usuario.username = :username "
		}
		hql += " order by mk.data desc"
		
		def query = getSession().createQuery(hql).setMaxResults(quantos)
		if (username) {
			query.setString "username", username
		}
		return query.list()
	}
}
