package br.com.bluesoft.pronto.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
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
		final List<Sprint> sprints = criteria.list();
		preencheTotaisDeEsforcoEValorDeNegocioDosSprints(sprints);
		return sprints;
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

	@SuppressWarnings("unchecked")
	private void preencheTotaisDeEsforcoEValorDeNegocioDosSprints(final Collection<Sprint> sprints) {
		final String sql = "select sprint, sum(t.valor_de_negocio) as valor_de_negocio_total, sum(t.esforco) as esforco_total from ticket t where t.sprint is not null group by sprint";
		final SQLQuery query = getSession().createSQLQuery(sql);
		query.addScalar("sprint", Hibernate.INTEGER);
		query.addScalar("valor_de_negocio_total", Hibernate.INTEGER);
		query.addScalar("esforco_total", Hibernate.DOUBLE);
		final List<Object[]> list = query.list();
		for (final Object[] o : list) {
			final Integer sprintKey = (Integer) o[0];
			final int valorDeNegocioTotal = (Integer) o[1];
			final double esforcoTotal = (Double) o[2];
			for (final Sprint s : sprints) {
				if (s.getSprintKey() == sprintKey) {
					s.setEsforcoTotal(esforcoTotal);
					s.setValorDeNegocioTotal(valorDeNegocioTotal);
				}

			}
		}
	}

}
