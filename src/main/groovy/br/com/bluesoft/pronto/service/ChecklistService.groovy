package br.com.bluesoft.pronto.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional

import br.com.bluesoft.pronto.dao.ChecklistDao
import br.com.bluesoft.pronto.dao.TicketDao
import br.com.bluesoft.pronto.model.Checklist
import br.com.bluesoft.pronto.model.ChecklistItem


@Service
class ChecklistService {

	@Autowired
	TicketDao ticketDao

	@Autowired
	ChecklistDao checklistDao

	@Transactional
	Checklist criarChecklist(int ticketKey, String nome) {
		if (nome && nome.size() > 0) {
			def checklist = new Checklist(nome:nome, ticket:ticketDao.obter(ticketKey))
			checklistDao.salvar checklist
			return checklist
		} else {
			throw new Exception("Informe o nome da checklist");
		}
	}

	@Transactional
	ChecklistItem incluirItem(int checklistKey, String descricaoDoItem) {
		def checklist = checklistDao.obter(checklistKey)
		def item = new ChecklistItem(descricao: descricaoDoItem, marcado:false, checklist:checklist)
		checklist.itens << item 
		checklistDao.salvar checklist
		return item
	}
	
	@Transactional
	void excluir(int checklistKey) {
		checklistDao.excluir(checklistDao.obter(checklistKey))
	}
	
	@Transactional
	boolean toogleItem(int checklistItemKey) {
		def item = checklistDao.obterItem(checklistItemKey)
		item.toogle()
		checklistDao.salvarItem(item)
		return item.marcado
	}
}
