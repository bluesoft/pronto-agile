package br.com.bluesoft.pronto.dao;

import java.util.Date;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.bluesoft.pronto.model.Categoria;

@Repository
class RelatorioDeQuantidadesDao  {
	
	@Autowired
	private SessionFactory sessionFactory
	
	def listarDefeitosPorCategoria(Date dataInicial, Date dataFinal, Integer tipoDeTicketKey) {
		def sql = """
			select c.descricao, count(*) 
			from ticket t
			inner join categoria c on c.categoria_key = t.categoria_key
			where ${tipoDeTicketKey > 0 ? 't.tipo_de_ticket_key = :tipoDeTicketKey' : '1=1'} 
			and data_de_criacao between :di and :df
			group by c.descricao
			order by count(*) desc
		"""
		
		def query = sessionFactory.currentSession.createSQLQuery(sql)
		query.setDate 'di', dataInicial
		query.setDate 'df', dataFinal
		if (tipoDeTicketKey > 0)
			query.setInteger 'tipoDeTicketKey', tipoDeTicketKey
		return query.list()
	}
	
	def listarDefeitosPorSprint(Date dataInicial, Date dataFinal,Integer tipoDeTicketKey) {
		def sql = """
			select c.nome, count(*)
			from ticket t
			inner join sprint c on c.sprint_key = t.sprint
			where ${tipoDeTicketKey > 0 ? 't.tipo_de_ticket_key = :tipoDeTicketKey' : '1=1'}
			and data_de_criacao between :di and :df
			group by c.nome
			order by count(*) desc
		"""
		
		def query = sessionFactory.currentSession.createSQLQuery(sql)
		query.setDate 'di', dataInicial
		query.setDate 'df', dataFinal
		if (tipoDeTicketKey > 0)
			query.setInteger 'tipoDeTicketKey', tipoDeTicketKey
		return query.list()
	}
	
	def listarDefeitosPorModulo(Date dataInicial, Date dataFinal,Integer tipoDeTicketKey) {
		def sql = """
			select c.descricao, count(*)
			from ticket t
			inner join modulo c on c.modulo_key = t.modulo_key
			where ${tipoDeTicketKey > 0 ? 't.tipo_de_ticket_key = :tipoDeTicketKey' : '1=1'}
			and data_de_criacao between :di and :df
			group by c.descricao
			order by count(*) desc
		"""
		
		def query = sessionFactory.currentSession.createSQLQuery(sql)
		query.setDate 'di', dataInicial
		query.setDate 'df', dataFinal
		if (tipoDeTicketKey > 0)
			query.setInteger 'tipoDeTicketKey', tipoDeTicketKey
		return query.list()
	}
	
	def listarDefeitosPorSemana(Date dataInicial, Date dataFinal,Integer tipoDeTicketKey) {
		def sql = """
			select semana, count(*), anomessemana from (
				select to_char(data_de_criacao,'w-MM/yyyy') as semana, to_number(to_char(data_de_criacao,'yyyyMMw'),'9999999') as anomessemana 
				from ticket t
				where ${tipoDeTicketKey > 0 ? 't.tipo_de_ticket_key = :tipoDeTicketKey' : '1=1'}
				and data_de_criacao between :di and :df
			) semanas 
			group by semana, anomessemana
			order by anomessemana asc	
		"""
		
		def query = sessionFactory.currentSession.createSQLQuery(sql)
		query.setDate 'di', dataInicial
		query.setDate 'df', dataFinal
		if (tipoDeTicketKey > 0)
			query.setInteger 'tipoDeTicketKey', tipoDeTicketKey
		return query.list()
	}
	
	def listarDefeitosPorMes(Date dataInicial, Date dataFinal,Integer tipoDeTicketKey) {
		def sql = """
			select mes, count(*), anomes from (
				select to_char(data_de_criacao,'MM/yyyy') as mes, to_number(to_char(data_de_criacao,'yyyyMM'),'999999') as anomes 
				from ticket t
				where ${tipoDeTicketKey > 0 ? 't.tipo_de_ticket_key = :tipoDeTicketKey' : '1=1'}
				and data_de_criacao between :di and :df
			) meses
			group by mes, anomes
			order by anomes asc
		"""
		
		def query = sessionFactory.currentSession.createSQLQuery(sql)
		query.setDate 'di', dataInicial
		query.setDate 'df', dataFinal
		if (tipoDeTicketKey > 0)
			query.setInteger 'tipoDeTicketKey', tipoDeTicketKey
		return query.list()
	}

	def listarDefeitosPorAno(Date dataInicial, Date dataFinal,Integer tipoDeTicketKey) {
		def sql = """
			select ano, count(*) from (
				select to_number(to_char(data_de_criacao,'yyyy'),'9999') as ano
				from ticket t
				where ${tipoDeTicketKey > 0 ? 't.tipo_de_ticket_key = :tipoDeTicketKey' : '1=1'}
				and data_de_criacao between :di and :df
			) anos
			group by ano
			order by ano asc
		"""
		
		def query = sessionFactory.currentSession.createSQLQuery(sql)
		query.setDate 'di', dataInicial
		query.setDate 'df', dataFinal
		if (tipoDeTicketKey > 0)
			query.setInteger 'tipoDeTicketKey', tipoDeTicketKey
		return query.list()
	}

		
	def listarDefeitosPorCliente(Date dataInicial, Date dataFinal,Integer tipoDeTicketKey) {
		def sql = """
			select c.nome, count(*)
			from ticket t
			inner join cliente c on c.cliente_key = t.cliente_key
			where ${tipoDeTicketKey > 0 ? 't.tipo_de_ticket_key = :tipoDeTicketKey' : '1=1'}
			and data_de_criacao between :di and :df
			group by c.nome
			order by count(*) desc
		"""
		
		def query = sessionFactory.currentSession.createSQLQuery(sql)
		query.setDate 'di', dataInicial
		query.setDate 'df', dataFinal
		if (tipoDeTicketKey > 0)
			query.setInteger 'tipoDeTicketKey', tipoDeTicketKey
		return query.list()
	}
	
}
