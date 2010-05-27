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
	
	MovimentoKanban movimentar(def ticket, int kanbanStatusKey) {
		movimentar ticket, kanbanStatusKey, null
	}
	
	MovimentoKanban movimentar(def ticket, int kanbanStatusKey, def motivoReprovacaoKey) {
		def status = kanbanStatusDao.obter(kanbanStatusKey)
		associarUsuario ticket, status
		validacoesDePronto ticket, status
		ticket.kanbanStatus = status
		ticketDao.salvar ticket
		return criarMovimento(ticket, motivoReprovacaoKey)
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
		if (status.kanbanStatusKey == KanbanStatus.DONE) {
			validarCausaDeDefeito(ticket) 
			ticket.dataDePronto = new Date()
		} else {
			ticket.dataDePronto = null
		}
	}
	
	private void validarCausaDeDefeito(def ticket) {
		if (ticket.defeito  && !ticket.causaDeDefeito) {
			throw new ProntoException("Antes de Mover um Defeito para DONE é preciso informar a Causa do Defeito.")
		}
	}
	
	private void associarUsuario(def ticket, def status) {
		if (status.kanbanStatusKey == KanbanStatus.DOING) {
			associarUsuarioComoDesenvolvedor(ticket);
		}
		
		if (status.kanbanStatusKey == KanbanStatus.TESTING) {
			associarUsuarioComoTestador(ticket);
		}
	}
	
	private void associarUsuarioComoDesenvolvedor(Ticket ticket){
		ticket.addDesenvolvedor(Seguranca.usuario);
	}
	
	private void associarUsuarioComoTestador(Ticket ticket){
		ticket.addTestador(Seguranca.usuario);
	}
	
}
