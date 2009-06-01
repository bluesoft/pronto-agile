package br.com.bluesoft.pronto.dao;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.bluesoft.pronto.model.Sprint;

@Repository
public class SprintDao extends DaoHibernate<Sprint, Integer> {

	@Autowired
	private SessionFactory sessionFactory;

	public SprintDao() {
		super(Sprint.class);
	}

	public Sprint getSprintAtual() {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Sprint.class);
		criteria.add(Restrictions.eq("atual", true));
		return (Sprint) criteria.uniqueResult();
	}

}
