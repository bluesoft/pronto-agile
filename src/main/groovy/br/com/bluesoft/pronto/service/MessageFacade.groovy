package br.com.bluesoft.pronto.service


import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import br.com.bluesoft.pronto.dao.ConfiguracaoDao
import br.com.bluesoft.pronto.model.MovimentoKanban
import br.com.bluesoft.pronto.model.Ticket

@Service
class MessageFacade {

	@Autowired
	ConfiguracaoDao configuracaoDao

	@Autowired BeanFactory factory

	MessageService getMailMessageService() {
		return factory.getBean("mailMessageService")
	}

	MessageService getJabberMessageService() {
		return factory.getBean("jabberMessageService")
	}

	boolean notificarMovimentacao(MovimentoKanban movimento) {
		def sbj = "#${movimento.ticket.ticketKey}: ${movimento.ticket.titulo}"
		def url = configuracaoDao.getProntoUrl() + "tickets/${movimento.ticket.ticketKey}"
		def msg = "${Seguranca.usuario} moveu o ticket #${movimento.ticket.ticketKey} para ${movimento.kanbanStatus.descricao}\n\n${url}"
		return this.enviarMensagem(sbj, msg, movimento.ticket.getEnvolvidosExcetoUsuarioLogado())
	}

	boolean enviarComentario(Ticket ticket, String comentario, def to) {
		def sbj = "#${ticket.ticketKey}: ${ticket.titulo}"
		def url = configuracaoDao.getProntoUrl() + "tickets/${ticket.ticketKey}"
		def msg = "${Seguranca.usuario} comenta:\n\n ${comentario}\n\n${url}"
		return this.enviarMensagem(sbj, msg, to)
	}

	void notificarImpedimento(Ticket ticket) {
		def sbj = "#${ticket.ticketKey}: ${ticket.titulo}"
		def url = configuracaoDao.getProntoUrl() + "tickets/${ticket.ticketKey}"
		def msg = "O ticket #${ticket.ticketKey} foi impedido por ${Seguranca.usuario} e ${ticket.responsavel} é o responsável por desimpedí-lo.\n\n${url}"
		this.enviarMensagem(sbj, msg, ticket.getTodosOsEnvolvidos());
	}

	void notificarDesimpedimento(Ticket ticket, def to) {
		def sbj = "#${ticket.ticketKey}: ${ticket.titulo}"
		def url = configuracaoDao.getProntoUrl() + "tickets/${ticket.ticketKey}"
		def msg = "O ticket #${ticket.ticketKey} foi desimpedido por ${Seguranca.usuario}.\n\n${url}"
		this.enviarMensagem(sbj, msg, to);
	}

	void enviarMensagem(String subject, String msg, def to) {
		this.getMailMessageService().enviarMensagem(subject, msg, to);
		this.getJabberMessageService().enviarMensagem(subject, msg, to);
	}
}
