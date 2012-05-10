package br.com.bluesoft.pronto.dao;

import java.util.Date;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.bluesoft.pronto.model.Categoria;

@Repository
class RelatorioDeTicketsDao  {
	
	@Autowired
	private SessionFactory sessionFactory
	
	def listarPorCategoria(Date dataInicial, Date dataFinal, Integer tipoDeTicketKey,referencia,valor) {
		def resultado = getResultado(valor)
		
		def sql = """
			select c.descricao, ${resultado} 
			from ticket t
			inner join categoria c on c.categoria_key = t.categoria_key
			where ${tipoDeTicketKey > 0 ? 't.tipo_de_ticket_key = :tipoDeTicketKey' : '1=1'} 
			and ${referencia} between :di and :df
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
	
	def listarPorSprint(Date dataInicial, Date dataFinal,Integer tipoDeTicketKey,referencia,valor) {
		def resultado = getResultado(valor)
		def sql = """
			select c.nome, ${resultado}
			from ticket t
			inner join sprint c on c.sprint_key = t.sprint
			where ${tipoDeTicketKey > 0 ? 't.tipo_de_ticket_key = :tipoDeTicketKey' : '1=1'}
			and ${referencia} between :di and :df
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
	
	def listarPorRelease(Date dataInicial, Date dataFinal,Integer tipoDeTicketKey,referencia,valor) {
		def resultado = getResultado(valor)
		def sql = """
			select t.release, ${resultado}
			from ticket t
			where ${tipoDeTicketKey > 0 ? 't.tipo_de_ticket_key = :tipoDeTicketKey' : '1=1'}
			and ${referencia} between :di and :df
			group by t.release
			order by count(*) desc
		"""
		
		def query = sessionFactory.currentSession.createSQLQuery(sql)
		query.setDate 'di', dataInicial
		query.setDate 'df', dataFinal
		if (tipoDeTicketKey > 0)
			query.setInteger 'tipoDeTicketKey', tipoDeTicketKey
		return query.list()
	}
	
	def listarPorModulo(Date dataInicial, Date dataFinal,Integer tipoDeTicketKey,referencia,valor) {
		def resultado = getResultado(valor)
		def sql = """
			select c.descricao, ${resultado}
			from ticket t
			inner join modulo c on c.modulo_key = t.modulo_key
			where ${tipoDeTicketKey > 0 ? 't.tipo_de_ticket_key = :tipoDeTicketKey' : '1=1'}
			and ${referencia} between :di and :df
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
	
	def listarPorEsforco(Date dataInicial, Date dataFinal,Integer tipoDeTicketKey,referencia,valor) {
		def resultado = getResultado(valor)
		def sql = """
			select t.esforco, ${resultado}
			from ticket t
			where ${tipoDeTicketKey > 0 ? 't.tipo_de_ticket_key = :tipoDeTicketKey' : '1=1'}
			and ${referencia} between :di and :df
			group by t.esforco
			order by count(*) desc
		"""
		
		def query = sessionFactory.currentSession.createSQLQuery(sql)
		query.setDate 'di', dataInicial
		query.setDate 'df', dataFinal
		if (tipoDeTicketKey > 0)
			query.setInteger 'tipoDeTicketKey', tipoDeTicketKey
		return query.list()
	}
	
	def listarPorValorDeNegocio(Date dataInicial, Date dataFinal,Integer tipoDeTicketKey,referencia,valor) {
		def resultado = getResultado(valor)
		def sql = """
			select t.valor_de_negocio, ${resultado}
			from ticket t
			where ${tipoDeTicketKey > 0 ? 't.tipo_de_ticket_key = :tipoDeTicketKey' : '1=1'}
			and ${referencia} between :di and :df
			group by t.valor_de_negocio
			order by count(*) desc
		"""
		
		def query = sessionFactory.currentSession.createSQLQuery(sql)
		query.setDate 'di', dataInicial
		query.setDate 'df', dataFinal
		if (tipoDeTicketKey > 0)
			query.setInteger 'tipoDeTicketKey', tipoDeTicketKey
		return query.list()
	}
	
	def listarPorSemana(Date dataInicial, Date dataFinal,Integer tipoDeTicketKey,referencia,valor) {
		def resultado = getResultado(valor)
		def sql = """
			select semana, ${resultado}, anomessemana from (
				select to_char(${referencia},'w-MM/yyyy') as semana, to_number(to_char(${referencia},'yyyyMMw'),'9999999') as anomessemana, t.* 
				from ticket t
				where ${tipoDeTicketKey > 0 ? 't.tipo_de_ticket_key = :tipoDeTicketKey' : '1=1'}
				and ${referencia} between :di and :df
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
	
	def listarPorMes(Date dataInicial, Date dataFinal,Integer tipoDeTicketKey,referencia,valor) {
		def resultado = getResultado(valor)
		def sql = """
			select mes, ${resultado}, anomes from (
				select to_char(${referencia},'MM/yyyy') as mes, to_number(to_char(${referencia},'yyyyMM'),'999999') as anomes, t.* 
				from ticket t
				where ${tipoDeTicketKey > 0 ? 't.tipo_de_ticket_key = :tipoDeTicketKey' : '1=1'}
				and ${referencia} between :di and :df
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

	def listarPorAno(Date dataInicial, Date dataFinal,Integer tipoDeTicketKey,referencia,valor) {
		def resultado = getResultado(valor)
		def sql = """
			select ano, ${resultado} from (
				select to_number(to_char(${referencia},'yyyy'),'9999') as ano, t.*
				from ticket t
				where ${tipoDeTicketKey > 0 ? 't.tipo_de_ticket_key = :tipoDeTicketKey' : '1=1'}
				and ${referencia} between :di and :df
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

		
	def listarPorCliente(Date dataInicial, Date dataFinal,Integer tipoDeTicketKey,referencia,valor) {
		def resultado = getResultado(valor)
		def sql = """
			select c.nome, ${resultado}
			from ticket t
			inner join cliente c on c.cliente_key = t.cliente_key
			where ${tipoDeTicketKey > 0 ? 't.tipo_de_ticket_key = :tipoDeTicketKey' : '1=1'}
			and ${referencia} between :di and :df
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

	def getResultado(valor){
		switch(valor){
			case 'quantidade':
				return 'count(*) as quantidade'
			case 'cycle': 
				return 'avg(EXTRACT(DAYS FROM (data_de_termino_do_ciclo - data_de_inicio_do_ciclo))) as cycle_time'
			case 'lead': 
				return 'avg(EXTRACT(DAYS FROM (coalesce(data_de_pronto,now()) - data_de_criacao))) as lead_time'
			case 'esforco':
				return 'sum(esforco)'
			case 'negocio':
				return 'sum(valor_de_negocio)'
		}
	}	
		
}
