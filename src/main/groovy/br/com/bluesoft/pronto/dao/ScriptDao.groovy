package br.com.bluesoft.pronto.dao

import java.util.List

import org.springframework.stereotype.Repository

import br.com.bluesoft.pronto.model.Execucao
import br.com.bluesoft.pronto.model.Script

@Repository
public class ScriptDao extends DaoHibernate{
	
	public ScriptDao() {
		super(Script.class)
	}

	public void removerExecucoesDoScript(final Script script) {
		if (script.getExecucoes() != null) {
			for (final Execucao execucao : script.getExecucoes()) {
				removerExecucao(execucao)
			}
		}
	}

	public void removerExecucao(final Execucao execucao) {
		final Script script = execucao.getScript()
		script.removerExecucao(execucao)
		getSession().delete(execucao)
	}

	public List<Script> listarComDependencias() {
		String hql = "select distinct s from Script s "
		hql += " left join fetch s.execucoes e "
		hql += " left join fetch s.ticket t "
		hql += " left join fetch t.cliente as c "
		hql += " left join fetch e.bancoDeDados b "
		hql += " left join fetch e.usuario b "
		hql += " left join fetch t.kanbanStatus k"
		return getSession().createQuery(hql).list()
	}
}
