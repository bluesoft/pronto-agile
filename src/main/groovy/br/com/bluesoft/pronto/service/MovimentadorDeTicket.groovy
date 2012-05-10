package br.com.bluesoft.pronto.service


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bluesoft.pronto.ProntoException;
import br.com.bluesoft.pronto.dao.KanbanStatusDao;
import br.com.bluesoft.pronto.dao.MotivoReprovacaoDao;
import br.com.bluesoft.pronto.dao.MovimentoKanbanDao;
import br.com.bluesoft.pronto.dao.TicketDao;
import br.com.bluesoft.pronto.model.MovimentoKanban;
import br.com.bluesoft.pronto.model.Ticket;

@Service
class MovimentadorDeTicket {

	@Autowired MovimentoKanbanDao movimentoKanbanDao
	@Autowired KanbanStatusDao kanbanStatusDao
	@Autowired TicketDao ticketDao
	@Autowired MotivoReprovacaoDao motivoReprovacaoDao
	@Autowired MessageFacade messenger
	
	MovimentoKanban movimentar(def ticket, int kanbanStatusKey) {
		movimentar ticket, kanbanStatusKey, null
	}
	
	@Transactional
	MovimentoKanban movimentar(Ticket ticket, int kanbanStatusKey, def motivoReprovacaoKey) {
		ticket = ticketDao.obter(ticket.ticketKey)
		def status = kanbanStatusDao.obter(kanbanStatusKey)
		validacoesDePronto ticket, status
		ticket.kanbanStatus = status
		envolverMovimentador(ticket)
		atualizarTempoDeCiclo(ticket, kanbanStatusKey)
		ticketDao.salvar ticket
		def movimento = criarMovimento(ticket, motivoReprovacaoKey) 
		messenger.notificarMovimentacao(movimento)
		return movimento
	}

	private envolverMovimentador(Ticket ticket) {
		ticket.addEnvolvido Seguranca.usuario
	}
	
	private atualizarTempoDeCiclo(Ticket ticket, int kanbanStatusKey){
		if (ticket.dataDeInicioDoCiclo == null && ticket.projeto.etapaDeInicioDoCiclo.kanbanStatusKey == kanbanStatusKey) {
			ticket.dataDeInicioDoCiclo = new Date()
		}
		if (ticket.projeto.etapaDeTerminoDoCiclo.kanbanStatusKey == kanbanStatusKey) {
			ticket.dataDeTerminoDoCiclo = new Date()
		}
	}

	private MovimentoKanban criarMovimento(Ticket ticket, def motivo) {
		MovimentoKanban movimento = new MovimentoKanban()
		movimento.ticket = ticket
		movimento.kanbanStatus = ticket.kanbanStatus
		movimento.data = new Date()
		movimento.usuario = Seguranca.usuario
		if (motivo) {
			movimento.motivoReprovacao = motivoReprovacaoDao.obter(motivo)
		}
		movimentoKanbanDao.salvar movimento
		return movimento
	}
	
	private void validacoesDePronto(Ticket ticket, status) {
		if (status.isFim()){
			validarCausaDeDefeito(ticket)
			validarChecklists(ticket) 
			ticket.dataDePronto = new Date()
		} else {
			ticket.dataDePronto = null
		}
	}
	
	private void validarCausaDeDefeito(Ticket ticket) {
		if (ticket.defeito  && !ticket.causaDeDefeito) {
			throw new ProntoException("Antes de marcar um defeito como resolvido é preciso informar sua causa.")
		}
	}
	
	private void validarChecklists(Ticket ticket) {
		def itensNaoMarcados = ticketDao.obterQuantidadeDeChecklistItemsNaoMarcadosPorTicket(ticket.ticketKey)
		if (itensNaoMarcados > 0) {
			throw new ProntoException("Não foi possível fechar o ticket porque há itens no checklist que não foram marcados/conferidos.")
		}
	}
	
}
