package br.com.bluesoft.pronto.dao

import java.util.List;


import org.hibernate.Criteria
import org.hibernate.criterion.Order
import org.springframework.stereotype.Repository

import br.com.bluesoft.pronto.model.Milestone


@Repository
class MilestoneDao extends DaoHibernate {

	MilestoneDao() {
		super(Milestone.class)
	}
	
	@Override
	public List<Milestone> listar() {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Milestone.class)
		criteria.addOrder(Order.asc("nome"))
		return criteria.list()
	}
	
	void excluir(Object... milestones) {
		String sql = "update ticket set milestone_key = null where milestone_key in (:milestones)"
		sessionFactory.currentSession.createSQLQuery(sql).setParameterList("milestones", milestones).executeUpdate()
		sessionFactory.currentSession.flush();
		super.excluir(milestones)
	}
}

