package br.com.bluesoft.pronto.dao

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.bluesoft.pronto.util.DateUtil;

@Repository
class DashboardDao {

	@Autowired SessionFactory sessionFactory

	private Session getSession() {
		return sessionFactory.getCurrentSession()
	}
	
	public Map<String, String> obterDefeitosParaCockpit() {
		String sql = "select count(*) from ticket where tipo_de_ticket_key = 3 and data_de_criacao >= current_date - :days"
		def result = [:]
		result['24h'] = getSession().createSQLQuery(sql).setInteger("days", 1).uniqueResult();
		result['48h'] = getSession().createSQLQuery(sql).setInteger('days', 2).uniqueResult();
		result['7d'] = getSession().createSQLQuery(sql).setInteger('days', 7).uniqueResult();
		result['14d'] = getSession().createSQLQuery(sql).setInteger('days', 14).uniqueResult();
		result['21d'] = getSession().createSQLQuery(sql).setInteger('days', 21).uniqueResult();
		result['30d'] = getSession().createSQLQuery(sql).setInteger('days', 30).uniqueResult();
		return result
	}
	
	public Map<String, String> obterDefeitosEntreguesParaCockpit() {
		String sql = "select count(*) from ticket where tipo_de_ticket_key = 3 and data_de_pronto >= current_date - :days"
		def result = [:]
		result['24h'] = getSession().createSQLQuery(sql).setInteger("days", 1).uniqueResult();
		result['48h'] = getSession().createSQLQuery(sql).setInteger('days', 2).uniqueResult();
		result['7d'] = getSession().createSQLQuery(sql).setInteger('days', 7).uniqueResult();
		result['14d'] = getSession().createSQLQuery(sql).setInteger('days', 14).uniqueResult();
		result['21d'] = getSession().createSQLQuery(sql).setInteger('days', 21).uniqueResult();
		result['30d'] = getSession().createSQLQuery(sql).setInteger('days', 30).uniqueResult();
		return result
	}
	
	public Map<String, String> obterEstoriasEntreguesParaCockpit() {
		String sql = "select count(*) from ticket where tipo_de_ticket_key = 2 and data_de_pronto >= current_date - :days"
		def result = [:]
		result['24h'] = getSession().createSQLQuery(sql).setInteger("days", 1).uniqueResult();
		result['48h'] = getSession().createSQLQuery(sql).setInteger('days', 2).uniqueResult();
		result['7d'] = getSession().createSQLQuery(sql).setInteger('days', 7).uniqueResult();
		result['14d'] = getSession().createSQLQuery(sql).setInteger('days', 14).uniqueResult();
		result['21d'] = getSession().createSQLQuery(sql).setInteger('days', 21).uniqueResult();
		result['30d'] = getSession().createSQLQuery(sql).setInteger('days', 30).uniqueResult();
		return result
	}
	
	public Map<String, String> obterEstoriasCriadasParaCockpit() {
		String sql = "select count(*) from ticket where tipo_de_ticket_key = 2 and data_de_criacao >= current_date - :days"
		def result = [:]
		result['24h'] = getSession().createSQLQuery(sql).setInteger("days", 1).uniqueResult();
		result['48h'] = getSession().createSQLQuery(sql).setInteger('days', 2).uniqueResult();
		result['7d'] = getSession().createSQLQuery(sql).setInteger('days', 7).uniqueResult();
		result['14d'] = getSession().createSQLQuery(sql).setInteger('days', 14).uniqueResult();
		result['21d'] = getSession().createSQLQuery(sql).setInteger('days', 21).uniqueResult();
		result['30d'] = getSession().createSQLQuery(sql).setInteger('days', 30).uniqueResult();
		return result
	}
}
