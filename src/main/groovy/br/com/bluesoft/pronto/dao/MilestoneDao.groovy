package br.com.bluesoft.pronto.dao

import org.springframework.stereotype.Repository

import br.com.bluesoft.pronto.model.Milestone


@Repository
class MilestoneDao extends DaoHibernate {

	MilestoneDao() {
		super(Milestone.class)
	}
	
	void excluir(Object... milestones) {
		String sql = "update ticket set milestone_key = null where milestone_key in (:milestones)"
		sessionFactory.currentSession.createSQLQuery(sql).setParameterList("milestones", milestones).executeUpdate()
		sessionFactory.currentSession.flush();
		super.excluir(milestones)
	}
}

