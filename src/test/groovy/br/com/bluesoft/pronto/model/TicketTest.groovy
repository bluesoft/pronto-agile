package br.com.bluesoft.pronto.model

import org.junit.Test
import br.com.bluesoft.pronto.util.DateUtil
import static org.junit.Assert.*

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
		assertTrue ticket.envolvidos.contains(reporter)
	}
	
	@Test
	void getEnvolvidosDeveRetornarOsDesenvolvedores() {
		def usuario = new Usuario(username:'andrefaria')
		def ticket = new Ticket(desenvolvedores:[usuario])
		assertTrue ticket.envolvidos.contains(usuario)
	}
	
	@Test
	void getEnvolvidosDeveRetornarOsTestadores() {
		def usuario = new Usuario(username:'andrefaria')
		def ticket = new Ticket(testadores:[usuario])
		assertTrue ticket.envolvidos.contains(usuario)
	}
	
	@Test
	void getEnvolvidosNaoDeveRepetirOMesmoUsuario() {
		def usuario = new Usuario(username:'andrefaria')
		def ticket = new Ticket(testadores:[usuario], desenvolvedores:[usuario], reporter:usuario)
		assertTrue ticket.envolvidos.contains(usuario)
		assertEquals 1, ticket.envolvidos.size()
	}
}
