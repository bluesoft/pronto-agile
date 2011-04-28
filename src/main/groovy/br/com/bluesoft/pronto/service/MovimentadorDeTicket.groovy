package br.com.bluesoft.pronto.service

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.bluesoft.pronto.ProntoException;
import br.com.bluesoft.pronto.core.KanbanStatus;
import br.com.bluesoft.pronto.dao.KanbanStatusDao;
import br.com.bluesoft.pronto.dao.MotivoReprovacaoDao;
import br.com.bluesoft.pronto.dao.MovimentoKanbanDao;
import br.com.bluesoft.pronto.dao.TicketDao;
import br.com.bluesoft.pronto.model.MotivoReprovacao;
import br.com.bluesoft.pronto.model.MovimentoKanban;
import br.com.bluesoft.pronto.model.Ticket;

@Service
class MovimentadorDeTicket {

	@Autowired MovimentoKanbanDao movimentoKanbanDao
	@Autowired KanbanStatusDao kanbanStatusDao
	@Autowired TicketDao ticketDao
	@Autowired MotivoReprovacaoDao motivoReprovacaoDao
	@Autowired JabberMessageService jabberMessageService
	
	MovimentoKanban movimentar(def ticket, int kanbanStatusKey) {
		movimentar ticket, kanbanStatusKey, null
	}
	
	MovimentoKanban movimentar(def ticket, int kanbanStatusKey, def motivoReprovacaoKey) {
		def status = kanbanStatusDao.obter(kanbanStatusKey)
		validacoesDePronto ticket, status
		ticket.kanbanStatus = status
		ticketDao.salvar ticket
		def movimento = criarMovimento(ticket, motivoReprovacaoKey) 
		jabberMessageService.notificarMovimentacao(movimento)
		return movimento
	}

	private MovimentoKanban criarMovimento(def ticket, def motivo) {
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
	
	private void validacoesDePronto(ticket, status) {
		if (status.isFim()){
			validarCausaDeDefeito(ticket) 
			ticket.dataDePronto = new Date()
		} else {
			ticket.dataDePronto = null
		}
	}
	
	private void validarCausaDeDefeito(def ticket) {
		if (ticket.defeito  && !ticket.causaDeDefeito) {
			throw new ProntoException("Antes de marcar um defeito como resolvido é preciso informar sua causa.")
		}
	}
	
}
