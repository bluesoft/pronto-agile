package br.com.bluesoft.pronto.service

import spock.lang.Specification
import br.com.bluesoft.pronto.dao.ChecklistDao
import br.com.bluesoft.pronto.dao.TicketDao
import br.com.bluesoft.pronto.model.Checklist
import br.com.bluesoft.pronto.model.Ticket

class ChecklistTest extends Specification {

	ChecklistService checklistService 
	
	def "criar uma nova checklist"() {
		
		given: 
		def ticketDao = Mock(TicketDao)
		def checklistDao = Mock(ChecklistDao)
		ticketDao.obter(1) >> new Ticket(ticketKey: 1)
		checklistService = new ChecklistService(ticketDao: ticketDao, checklistDao:checklistDao)
		
		when: "inserir um novo checklist"
		def checklist = checklistService.criarChecklist(1, "testar")
		
		then: "ele ser associado ao ticket"
		checklist.ticket.ticketKey == 1
		
		and: "salvo no banco de dados"
		1 * checklistDao.salvar(_)
		
		when: "inserir um novo checklist sem nome"
		checklist = checklistService.criarChecklist(1, "")
		
		then: "uma exception deverá ser lançada"
		def e = thrown(Exception)
		
	}
	
	def "incluir um item na checklist"() {
		
		given:
		def checklistDao = Mock(ChecklistDao)
		checklistService = new ChecklistService(checklistDao:checklistDao)
		def checklist = new Checklist(checklistKey: 1)
		checklistDao.obter(checklist.checklistKey) >> checklist 
		
		when: "a inclusao de um novo item for solicitado"
		checklistService.incluirItem(checklist.checklistKey, "verificar se funciona no firefox")
		
		then: "deve-se adicioná-lo na checklist"
		checklist.itens.size() == 1
		
		and: "com a flag marcado falsa"
		checklist.itens[0].marcado == false
	}
}
