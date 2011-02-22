package br.com.bluesoft.pronto.dao

import java.util.Collection
import java.util.List

import org.hibernate.Criteria
import org.hibernate.Hibernate
import org.hibernate.SQLQuery
import org.hibernate.SessionFactory
import org.hibernate.criterion.Order
import org.hibernate.criterion.Restrictions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

import br.com.bluesoft.pronto.model.Sprint

@Repository
public class SprintDao extends DaoHibernate{

	@Autowired
	private SessionFactory sessionFactory

	public SprintDao() {
		super(Sprint.class)
	}

	@Override
	public Sprint obter(final Integer key) {
		final Sprint sprint = super.obter(key)
		return sprint
	}

	@Override
	public List<Sprint> listar() {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Sprint.class)
		criteria.addOrder(Order.desc("atual")).addOrder(Order.desc("dataFinal"))
		
		final List<Sprint> sprints = criteria.list()
		preencheTotaisDeEsforcoEValorDeNegocioDosSprints(sprints)
		
		return sprints
	}

	public List<Sprint> listarSprintsEmAberto() {
		return listarSprints(false)
	}
	
	public List<Sprint> listarSprintsFechados() {
		return listarSprints(true)
	}
	
	private List<Sprint> listarSprints(final boolean fechado) {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Sprint.class)
		criteria.add(Restrictions.eq("fechado", fechado))
		criteria.addOrder(Order.desc("atual")).addOrder(Order.asc("nome"))
		
		final List<Sprint> sprints = criteria.list()
		preencheTotaisDeEsforcoEValorDeNegocioDosSprints(sprints)
		
		return sprints
	}
	
	private void preencheTotaisDeEsforcoEValorDeNegocioDosSprints(final Collection<Sprint> sprints) {
		final String sql = """
			select s.sprint_key, a.valor_de_negocio_total,
				   coalesce(b.esforco_tarefas,0) + coalesce(c.esforco_estorias,0) as esforco_total
			from sprint s
			left join
			( select sprint, sum(valor_de_negocio) as valor_de_negocio_total
			  from ticket
			  where pai is null
			  group by sprint
			) a on s.sprint_key = a.sprint
			left join
			( select sprint, sum(esforco) as esforco_tarefas
			  from ticket
			  where pai is not null
			  group by sprint
			) b on s.sprint_key = b.sprint
			left join
			( select t.sprint, sum(t.esforco) as esforco_estorias
			  from ticket t
			  where t.pai is null
			  and t.ticket_key not in
			  ( select pai from ticket where sprint = t.sprint and pai is not null )
			  group by t.sprint
			) c on s.sprint_key = c.sprint		
		"""
		
		final SQLQuery query = getSession().createSQLQuery(sql)
		query.addScalar("sprint_key", Hibernate.INTEGER)
		query.addScalar("valor_de_negocio_total", Hibernate.INTEGER)
		query.addScalar("esforco_total", Hibernate.DOUBLE)
		final List<Object[]> list = query.list()
		
		for (final Object[] o : list) {
			final Integer sprintKey = (Integer) o[0]
			final int valorDeNegocioTotal = (Integer) o[1]
			final double esforcoTotal = (Double) o[2]
		
			for (final Sprint s : sprints) {
				if (s.getSprintKey() == sprintKey) {
					s.setEsforcoTotal(esforcoTotal)
					s.setValorDeNegocioTotal(valorDeNegocioTotal)
				}
			}
		}
	}

	public Sprint getSprintAtual() {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Sprint.class)
		criteria.add(Restrictions.eq("atual", true))
		final Sprint sprint = (Sprint) criteria.uniqueResult()
		return sprint
	}

	public Sprint getSprintAtualComTickets() {
		final String hql = """
			select distinct s 
			from Sprint s 
			left join fetch s.tickets t 
			left join fetch t.filhos f 
			left join fetch t.backlog
			left join fetch t.cliente
			left join fetch t.script
			left join fetch t.tipoDeTicket
			left join fetch t.kanbanStatus
			where s.atual = true
		"""
		final Sprint sprint = (Sprint) getSession().createQuery(hql).uniqueResult()
		return sprint
	}

	public Sprint obterSprintComTicket(final Integer sprintKey) {
		final String hql = """
			select distinct s 
			from Sprint s 
			left join fetch s.tickets t 
			left join fetch t.filhos f
			left join fetch t.backlog
			left join fetch t.tipoDeTicket
			left join fetch t.kanbanStatus
			where s.sprintKey = :sprintKey
		""" 
		
		final Sprint sprint = (Sprint) getSession().createQuery(hql).setInteger("sprintKey", sprintKey).uniqueResult()
		return sprint
	}
}
