package br.com.bluesoft.pronto.dao;

import java.util.Date;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.bluesoft.pronto.model.Categoria;

@Repository
class RelatorioDeDefeitosDao  {
	
	@Autowired
	private SessionFactory sessionFactory
	
	def listarDefeitosPorCategoria(Date dataInicial, Date dataFinal) {
		def sql = """
			select c.descricao, count(*) 
			from ticket t
			inner join categoria c on c.categoria_key = t.categoria_key
			where t.tipo_de_ticket_key = 3
			and data_de_criacao between :di and :df
			group by c.descricao
			order by count(*) desc
		"""
		
		def query = sessionFactory.currentSession.createSQLQuery(sql)
		query.setDate 'di', dataInicial
		query.setDate 'df', dataFinal
		return query.list()
	}
	
	def listarDefeitosPorSprint(Date dataInicial, Date dataFinal) {
		def sql = """
			select c.nome, count(*)
			from ticket t
			inner join sprint c on c.sprint_key = t.sprint
			where t.tipo_de_ticket_key = 3
			and data_de_criacao between :di and :df
			group by c.nome
			order by count(*) desc
		"""
		
		def query = sessionFactory.currentSession.createSQLQuery(sql)
		query.setDate 'di', dataInicial
		query.setDate 'df', dataFinal
		return query.list()
	}
	
	def listarDefeitosPorModulo(Date dataInicial, Date dataFinal) {
		def sql = """
			select c.descricao, count(*)
			from ticket t
			inner join modulo c on c.modulo_key = t.modulo_key
			where t.tipo_de_ticket_key = 3
			and data_de_criacao between :di and :df
			group by c.descricao
			order by count(*) desc
		"""
		
		def query = sessionFactory.currentSession.createSQLQuery(sql)
		query.setDate 'di', dataInicial
		query.setDate 'df', dataFinal
		return query.list()
	}
	
	def listarDefeitosPorCliente(Date dataInicial, Date dataFinal) {
		def sql = """
			select c.nome, count(*)
			from ticket t
			inner join cliente c on c.cliente_key = t.cliente_key
			where t.tipo_de_ticket_key = 3
			and data_de_criacao between :di and :df
			group by c.nome
			order by count(*) desc
		"""
		
		def query = sessionFactory.currentSession.createSQLQuery(sql)
		query.setDate 'di', dataInicial
		query.setDate 'df', dataFinal
		return query.list()
	}
	
}
