/*
 * Copyright 2009 Pronto Agile Project Management.
 * This file is part of Pronto.
 * Pronto is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Pronto is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Pronto. If not, see <http://www.gnu.org/licenses/>.
 */

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
	public Sprint obter(final Integer key) {
		final Sprint sprint = super.obter(key);
		preencheTotaisDeEsforcoEValorDeNegocioDoSprint(sprint);
		return sprint;
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

	@SuppressWarnings("unchecked")
	public List<Sprint> listarSprintsEmAberto() {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Sprint.class);
		criteria.add(Restrictions.eq("fechado", false));
		final List<Sprint> sprints = criteria.list();
		preencheTotaisDeEsforcoEValorDeNegocioDosSprints(sprints);
		return sprints;
	}

	@SuppressWarnings("unchecked")
	private void preencheTotaisDeEsforcoEValorDeNegocioDosSprints(final Collection<Sprint> sprints) {
		final String sql = "select sprint, sum(t.valor_de_negocio) as valor_de_negocio_total, sum(t.esforco) as esforco_total from ticket t where t.sprint is not null and t.pai is null group by sprint";
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

	private void preencheTotaisDeEsforcoEValorDeNegocioDoSprint(final Sprint sprint) {
		final String sql = "select sprint, sum(t.valor_de_negocio) as valor_de_negocio_total, sum(t.esforco) as esforco_total from ticket t where t.sprint = :sprint and t.pai is null group by sprint";
		final SQLQuery query = getSession().createSQLQuery(sql);
		query.setInteger("sprint", sprint.getSprintKey());
		query.addScalar("sprint", Hibernate.INTEGER);
		query.addScalar("valor_de_negocio_total", Hibernate.INTEGER);
		query.addScalar("esforco_total", Hibernate.DOUBLE);
		final Object[] o = (Object[]) query.uniqueResult();

		int valorDeNegocioTotal = 0;
		double esforcoTotal = 0d;

		if (o != null) {
			valorDeNegocioTotal = (Integer) o[1];
			esforcoTotal = (Double) o[2];
		}

		sprint.setEsforcoTotal(esforcoTotal);
		sprint.setValorDeNegocioTotal(valorDeNegocioTotal);

	}

	public Sprint getSprintAtual() {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Sprint.class);
		criteria.add(Restrictions.eq("atual", true));
		final Sprint sprint = (Sprint) criteria.uniqueResult();
		if (sprint != null && sprint.getQuantidadeDeTickets() > 0) {
			preencheTotaisDeEsforcoEValorDeNegocioDoSprint(sprint);
		}
		return sprint;
	}

	public Sprint getSprintAtualComTickets() {
		final String hql = "select distinct s from Sprint s left join fetch s.tickets t left join fetch t.filhos f where s.atual = true";
		final Sprint sprint = (Sprint) getSession().createQuery(hql).uniqueResult();
		if (sprint != null && sprint.getQuantidadeDeTickets() > 0) {
			preencheTotaisDeEsforcoEValorDeNegocioDoSprint(sprint);
		}
		return sprint;
	}

	public Sprint obterSprintComTicket(final Integer sprintKey) {
		final String hql = "select distinct s from Sprint s left join fetch s.tickets t left join fetch t.filhos f where s.sprintKey = :sprintKey";
		final Sprint sprint = (Sprint) getSession().createQuery(hql).setInteger("sprintKey", sprintKey).uniqueResult();
		preencheTotaisDeEsforcoEValorDeNegocioDoSprint(sprint);
		return sprint;
	}
}
