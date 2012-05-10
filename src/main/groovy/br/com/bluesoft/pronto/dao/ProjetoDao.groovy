package br.com.bluesoft.pronto.dao

import java.util.List
import org.springframework.stereotype.Repository
import br.com.bluesoft.pronto.model.Projeto

@Repository
class ProjetoDao extends DaoHibernate {

	
	ProjetoDao() {
		super(Projeto.class)
	}

	List<Projeto> listar() {
		String hql = "from Projeto as p order by p.nome"
		return getSession().createQuery(hql).list()
	}
	
	List<Projeto> listarProjetosComSprintsAtivos() {
		String hql = "select distinct p from Projeto as p inner join fetch p.sprints s where s.fechado != true order by p.nome, s.nome"
		return getSession().createQuery(hql).list()
	}
	
	void atualizarTempoDeCiclo(int projetoKey){
		def sqlInicio = """ 
		update ticket x set data_de_inicio_do_ciclo = (
			select coalesce(min(m.data), min(t.data_de_criacao)) as data from ticket t 
				left join projeto p on t.projeto_key = p.projeto_key
				left join movimento_kanban m on m.ticket_key = t.ticket_key and m.kanban_status_key = p.etapa_de_inicio_do_ciclo_key
			where x.ticket_key = t.ticket_key
			and x.projeto_key = :projetoKey
			group by t.ticket_key
		) where projeto_key = :projetoKey
		"""
		
		getSession().createSQLQuery(sqlInicio).setInteger('projetoKey',projetoKey).executeUpdate()
		
		def sqlTermino = """
			update ticket x set data_de_termino_do_ciclo = (
				select coalesce(min(m.data), min(t.data_de_pronto)) as data from ticket t 
					left join projeto p on t.projeto_key = p.projeto_key
					left join movimento_kanban m on m.ticket_key = t.ticket_key and m.kanban_status_key = p.etapa_de_termino_do_ciclo_key
				where x.ticket_key = t.ticket_key
				and x.projeto_key = :projetoKey
				group by t.ticket_key
			) where projeto_key = :projetoKey
		"""
		getSession().createSQLQuery(sqlTermino).setInteger('projetoKey',projetoKey).executeUpdate()
	}
}

