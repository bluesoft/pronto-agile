package br.com.bluesoft.pronto.service

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*

import br.com.bluesoft.pronto.model.Ticket;
import br.com.bluesoft.pronto.model.TicketComentario;
import br.com.bluesoft.pronto.util.DateUtil;

class GeradorDeLogDeTicketTest {
	
	GeradorDeLogDeTicket gerador;
	
	@Before
	void setUp(){
		this.gerador = new GeradorDeLogDeTicket()
	}
	
	@Test
	void geradorDeLogDeTicketDeveGerarUmTicketLogParaCadaDiferencaEntreOsTickets() {
		
		def velho = new Ticket()
		velho.descricao = 'Ovo de Pascoa'
		velho.esforco = 30
		
		def novo = new Ticket()
		novo.descricao = 'Caixa de Fosforos'
		
		def logs = gerador.gerarLogs(velho, novo);
		
		assertEquals 2, logs.size()
		assertEquals 'descrição', logs[0].campo
		assertEquals 'esforço', logs[1].campo
	}
	
	@Test
	void geradorDeLogDeTicketDeveCriarLogComMensagemEmBrancoQuandoOValorForNull() {
		
		def velho = new Ticket()
		velho.descricao = 'Ovo de Pascoa'
		
		def novo = new Ticket()
		novo.descricao = null
		
		def logs = gerador.gerarLogs(velho, novo);
		
		assertEquals 1, logs.size()
		assertEquals 'Ovo de Pascoa', logs[0].valorAntigo
		assertEquals 'em branco', logs[0].valorNovo
		
	}
	
	@Test
	void geradorDeLogDeTicketDeveCriarLogComDatasFormatadasQuandoForCamposDeData() {
		
		def dataDoAniversarioDeAndreFaria = DateUtil.toDate('16/06/1986')
		
		def velho = new Ticket()
		velho.dataDePronto = null
		
		def novo = new Ticket()
		novo.dataDePronto = dataDoAniversarioDeAndreFaria 
		
		def logs = gerador.gerarLogs(velho, novo);
		
		assertEquals 1, logs.size()
		assertEquals 'em branco', logs[0].valorAntigo
		assertEquals '16/06/1986', logs[0].valorNovo  
		
	}
	
	@Test
	void geradorDeLogDeTicketDeveIgnorarCamposQueNaoPossuamAAnnotationAuditable() {
		
		def velho = new Ticket()
		velho.comentarios = []
		
		def novo = new Ticket()
		novo.comentarios = []
		novo.comentarios.add new TicketComentario()
		
		def logs = gerador.gerarLogs(velho, novo);
		
		assertEquals logs.size(), 0
		  
		
	}
	
	
}
