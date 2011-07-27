package br.com.bluesoft.pronto.dao

import java.util.List

import org.springframework.stereotype.Repository

import br.com.bluesoft.pronto.model.Execucao
import br.com.bluesoft.pronto.model.Script

@Repository
public class ScriptDao extends DaoHibernate{
	
	ScriptDao() {
		super(Script.class)
	}

	void removerExecucoesDoScript(Script script) {
		script.execucoes.clear()
	}

	void removerExecucao(Execucao execucao) {
		final Script script = execucao.script
		script.removerExecucao execucao
		getSession().delete execucao
	}

	
	List<Script> listarComDependencias() {
		return listarComDependencias(null)
	}
	
	List<Script> listarComDependencias(Integer kanbanStatusKey) {
		String hql = """
			select distinct s from Script s 
			left join fetch s.ticket t 
			left join fetch t.cliente as c 
			left join fetch t.kanbanStatus k
			left join fetch t.backlog
			left join fetch t.tipoDeTicket
			left join fetch t.sprint
			where 1=1
		"""
		if (kanbanStatusKey && kanbanStatusKey > 0) {
			hql += "and (k.kanbanStatusKey is null or k.kanbanStatusKey = ${kanbanStatusKey}) "
		}
		
		hql += "order by s.scriptKey"
		return getSession().createQuery(hql).list()
	}
	
	List<Script> listarPendentesComDependencias() {
		return listarPendentesComDependencias(null)
	}
	
	List<Script> listarPendentesComDependencias(Integer kanbanStatusKey) {
		String hql = """
			select distinct s from Script s 
			left join fetch s.ticket t 
			left join fetch t.cliente as c 
			left join fetch t.kanbanStatus k
			left join fetch t.backlog
			left join fetch t.tipoDeTicket
			left join fetch t.sprint
			where s.execucoesPendentes > 0
		"""
		if (kanbanStatusKey && kanbanStatusKey > 0) {
			hql += "and (k.kanbanStatusKey is null or k.kanbanStatusKey = ${kanbanStatusKey}) "
		}
		hql += "order by s.scriptKey"
		
		//tem que cruzar as execucoes por dentro e por fora (e,x) senão não dá pra exibir quantas faltam.
		return getSession().createQuery(hql).list()
	}
	
	List<Script> listarExecutadosComDependencias() {
		return listarExecutadosComDependencias(null)
	}
	
	List<Script> listarExecutadosComDependencias(Integer kanbanStatusKey) {
		String hql = """
			select distinct s from Script s  
			left join fetch s.ticket t 
			left join fetch t.cliente as c 
			left join fetch t.kanbanStatus k
			left join fetch t.backlog
			left join fetch t.tipoDeTicket
			left join fetch t.sprint
			where s.execucoesPendentes = 0
		"""
		if (kanbanStatusKey && kanbanStatusKey > 0) {
			hql += "and (k.kanbanStatusKey is null or k.kanbanStatusKey = ${kanbanStatusKey}) "
		}
		hql += "order by s.scriptKey"
		return getSession().createQuery(hql).list()
	}
	
	void atualizaTotalDeExecucoes(Script script) {
		script.setTotalDeExecucoes script.execucoes == null ? 0 : script.execucoes.size()
	}
	
	void atualizaExecucoesPendentes(Script script, int execucoesPendentes) {
		script.setExecucoesPendentes execucoesPendentes
	}
	
	int getQuantidadeDeExecucoesPendentes(Script script) {
		
		int quantidadeDeExecucoesPendentes = 0
		
		for (Execucao execucao : script.execucoes) {
			if(execucao.isPendente()) {
				quantidadeDeExecucoesPendentes++
			}
		}
		
		return quantidadeDeExecucoesPendentes
	}
}
