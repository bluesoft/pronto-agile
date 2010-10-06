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
		return obterComExecucoes(bancoDeDadosKey,null)
	}
	
	BancoDeDados obterComExecucoes(int bancoDeDadosKey, Integer kanbanStatusKey) {
		
		String hql = """
			select distinct b 
			from BancoDeDados b 
			inner join fetch b.execucoes e
			inner join fetch e.script s
			left join fetch s.ticket t
			left join fetch t.kanbanStatus k
			left join fetch t.backlog
			where b.bancoDeDadosKey = :bancoDeDadosKey
		"""
		
		if (kanbanStatusKey && kanbanStatusKey > 0) {
			hql += " and (k.kanbanStatusKey is null or k.kanbanStatusKey = ${kanbanStatusKey})"	
		}
		
		getSession().createQuery(hql).setInteger('bancoDeDadosKey', bancoDeDadosKey).uniqueResult()
	}
	
	BancoDeDados obterComExecucoesPendentes(int bancoDeDadosKey) {
		return obterComExecucoesPendentes(bancoDeDadosKey, null)
	}
	
	BancoDeDados obterComExecucoesPendentes(int bancoDeDadosKey, Integer kanbanStatusKey) {
		String hql = """
			select distinct b 
			from BancoDeDados b 
			inner join fetch b.execucoes e
			inner join fetch e.script s
			left join fetch s.ticket t
			left join fetch t.kanbanStatus k
			left join fetch t.backlog
			where b.bancoDeDadosKey = :bancoDeDadosKey 
			and e.data is null
		"""
		
		if (kanbanStatusKey && kanbanStatusKey > 0) {
			hql += " and (k.kanbanStatusKey is null or k.kanbanStatusKey = ${kanbanStatusKey})"
		}
		
		getSession().createQuery(hql).setInteger('bancoDeDadosKey', bancoDeDadosKey).uniqueResult()
	}
	
	List listar() {
		getSession().createQuery("from BancoDeDados b order by b.nome").list()
	}
	
	
	List listarComExecucoes() {
		return listarComExecucoes(null)
	}
	
	List listarComExecucoes(kanbanStatusKey) {
		String hql = """ 
			select distinct b
			from BancoDeDados b 
			inner join fetch b.execucoes e
			inner join fetch e.script s
			left join fetch s.ticket t
			left join fetch t.kanbanStatus k
			left join fetch t.backlog
			where 1=1
		"""
		if (kanbanStatusKey && kanbanStatusKey > 0) {
			hql += " and (k.kanbanStatusKey is null or k.kanbanStatusKey = ${kanbanStatusKey})"
		}
		hql+=" order by b.nome, s.scriptKey"
		return getSession().createQuery(hql).list()
	}

	List listarComExecucoesPendentes() {
		return listarComExecucoesPendentes(null) 
	}
	
	List listarComExecucoesPendentes(kanbanStatusKey) {
		String hql = """ 
			select distinct b
			from BancoDeDados b 
			inner join fetch b.execucoes e
			inner join fetch e.script s
			left join fetch s.ticket t
			left join fetch t.kanbanStatus k
			left join fetch t.backlog
			where e.data is null
		"""
		
		if (kanbanStatusKey && kanbanStatusKey > 0) {
			hql += " and (k.kanbanStatusKey is null or k.kanbanStatusKey = ${kanbanStatusKey})"
		}
		
		hql+=" order by b.nome, s.scriptKey "
		
		return getSession().createQuery(hql).list()
	}
	

}
