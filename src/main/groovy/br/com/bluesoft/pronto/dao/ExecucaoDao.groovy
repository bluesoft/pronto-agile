package br.com.bluesoft.pronto.dao

import br.com.bluesoft.pronto.model.Execucao

import org.hibernate.Query
import org.springframework.stereotype.Repository

@Repository
class ExecucaoDao extends DaoHibernate {
	
	ExecucaoDao() {
		super(Execucao.class)
	}
	
	List listarPorKeys(Collection keys) {
		String hql = 'select e from Execucao e inner join fetch e.script where e.execucaoKey in ( :execucaoKey )'
		Query query = getSession().createQuery(hql)
		query.setParameterList("execucaoKey", keys)
		query.list()
	}

}