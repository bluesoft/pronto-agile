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
	
}
