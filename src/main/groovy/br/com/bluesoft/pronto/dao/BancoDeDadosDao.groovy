package br.com.bluesoft.pronto.dao

import java.util.List

import org.springframework.stereotype.Repository

import br.com.bluesoft.pronto.model.BancoDeDados

@Repository
public class BancoDeDadosDao extends DaoHibernate {

	BancoDeDadosDao() {
		super(BancoDeDados.class)
	}

	BancoDeDados obterComExecucoes(int bancoDeDadosKey) {
		String hql = """
			select distinct b 
			from BancoDeDados b 
			inner join fetch b.execucoes e
			inner join fetch e.script s
			left join fetch s.ticket t
			left join fetch t.kanbanStatus
			left join fetch t.backlog
			where b.bancoDeDadosKey = :bancoDeDadosKey
		"""
		getSession().createQuery(hql).setInteger('bancoDeDadosKey', bancoDeDadosKey).uniqueResult()
	}
	
	BancoDeDados obterComExecucoesPendentes(int bancoDeDadosKey) {
		String hql = """
			select distinct b 
			from BancoDeDados b 
			inner join fetch b.execucoes e
			inner join fetch e.script s
			left join fetch s.ticket t
			left join fetch t.kanbanStatus
			left join fetch t.backlog
			where b.bancoDeDadosKey = :bancoDeDadosKey 
			and e.data is null
		"""
		getSession().createQuery(hql).setInteger('bancoDeDadosKey', bancoDeDadosKey).uniqueResult()
	}
	
	List listar() {
		getSession().createQuery("from BancoDeDados b order by b.nome").list()
	}
	
	List listarComExecucoes() {
		String hql = """ 
			select distinct b
			from BancoDeDados b 
			inner join fetch b.execucoes e
			inner join fetch e.script s
			left join fetch s.ticket t
			left join fetch t.kanbanStatus
			left join fetch t.backlog
			order by b.nome 
		"""
		
		return getSession().createQuery(hql).list()
	}

	List listarComExecucoesPendentes() {
		String hql = """ 
			select distinct b
			from BancoDeDados b 
			inner join fetch b.execucoes e
			inner join fetch e.script s
			left join fetch s.ticket t
			left join fetch t.kanbanStatus
			left join fetch t.backlog
			where e.data is null
			order by b.nome 
		"""
		
		return getSession().createQuery(hql).list()
	}
	

}
