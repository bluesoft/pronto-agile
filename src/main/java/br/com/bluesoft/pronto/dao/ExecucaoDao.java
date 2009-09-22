package br.com.bluesoft.pronto.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.bluesoft.pronto.model.Execucao;

@Repository
public class ExecucaoDao extends DaoHibernate<Execucao, Integer> {

	public ExecucaoDao() {
		super(Execucao.class);
	}

	@SuppressWarnings("unchecked")
	public List<Execucao> listar(final Integer[] execucaoKey) {
		final String hql = "select e from Execucao e inner join fetch e.script where e.execucaoKey in ( :execucaoKey )";
		return getSession().createQuery(hql).setParameterList("execucaoKey", execucaoKey).list();
	}

}
