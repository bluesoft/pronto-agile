package br.com.bluesoft.pronto.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
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

	@Override
	@SuppressWarnings("unchecked")
	public List<Sprint> listar() {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Sprint.class);
		criteria.addOrder(Order.desc("atual")).addOrder(Order.desc("dataFinal"));
		return criteria.list();
	}

	public Sprint getSprintAtual() {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Sprint.class);
		criteria.add(Restrictions.eq("atual", true));
		return (Sprint) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Sprint> listarSprintsEmAberto() {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Sprint.class);
		criteria.add(Restrictions.eq("fechado", false));
		return criteria.list();
	}

}
