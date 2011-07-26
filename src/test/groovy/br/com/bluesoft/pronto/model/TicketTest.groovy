package br.com.bluesoft.pronto.model

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test

import br.com.bluesoft.pronto.service.Seguranca

class TicketTest {

	@Test
	void getTempoDeVidaEmDiasDeveUsarADataAtualSeOTicketNaoEstiverPronto() {
		Ticket t = new Ticket()
		t.setDataDeCriacao(new Date() - 1)
		assertEquals 1, t.getTempoDeVidaEmDias()
	}
	
	@Test
	void getTempoDeVidaEmDiasDeveUsarADataDeProntoSeOTicketJaEstiverPronto() {
		Ticket t = new Ticket()
		t.setDataDeCriacao(new Date() - 100)
		t.setDataDePronto(new Date() - 50)
		assertEquals 50, t.getTempoDeVidaEmDias()
	}
	
	@Test
	void getEnvolvidosDeveRetornarOReporter() {
		def reporter = new Usuario(username:'andrefaria')
		def ticket = new Ticket(reporter:reporter)
		assertTrue ticket.todosOsEnvolvidos.contains(reporter)
	}
	
	@Test
	void getEnvolvidosDeveResponsavelOReporter() {
		def responsavel = new Usuario(username:'andrefaria')
		def ticket = new Ticket(responsavel:responsavel)
		assertTrue ticket.todosOsEnvolvidos.contains(responsavel)
	}
	
	@Test
	void getEnvolvidosNaoDeveRepetirOMesmoUsuario() {
		def usuario = new Usuario(username:'andrefaria')
		def ticket = new Ticket(envolvidos:[usuario], reporter:usuario, responsavel:usuario)
		def todos = ticket.todosOsEnvolvidos
		assertTrue todos.contains(usuario)
		assertEquals 1, todos.size()
	}
}
