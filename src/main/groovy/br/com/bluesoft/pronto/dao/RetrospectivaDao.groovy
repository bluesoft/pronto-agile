package br.com.bluesoft.pronto.dao

import org.springframework.stereotype.Repository

import br.com.bluesoft.pronto.model.Retrospectiva

@Repository
public class RetrospectivaDao extends DaoHibernate {

	RetrospectivaDao() {
		super(Retrospectiva.class)
	}

	public Retrospectiva obterRetrospectivaDoSprint(final int sprintKey) {
		final String hql = "from Retrospectiva r where r.sprint.sprintKey = :sprintKey"
		return (Retrospectiva) getSession().createQuery(hql).setInteger("sprintKey", sprintKey).uniqueResult()
	}

}
