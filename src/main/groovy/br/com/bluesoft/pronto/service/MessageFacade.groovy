package br.com.bluesoft.pronto.service


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import br.com.bluesoft.pronto.dao.ConfiguracaoDao
import br.com.bluesoft.pronto.model.MovimentoKanban

@Service
class MessageFacade {

	@Autowired
	ConfiguracaoDao configuracaoDao
	
	@Autowired
	JabberMessageService jabberMessageService

	@Autowired
	MailMessageService mailMessageService

	boolean notificarMovimentacao(MovimentoKanban movimento) {
		def sbj = "${Seguranca.usuario} movimentou o ticket #${movimento.ticket.ticketKey}";
		def url = configuracaoDao.getProntoUrl() + "tickets/${movimento.ticket.ticketKey}"
		def msg = "${Seguranca.usuario} moveu o ticket #${movimento.ticket.ticketKey} para ${movimento.kanbanStatus.descricao}\n${url}"
		return this.enviarMensagem(sbj, msg, movimento.ticket.envolvidos)
	}

	boolean enviarComentario(int ticketKey, String comentario, def to) {
		def sbj = "${Seguranca.usuario} comentou no ticket #${ticketKey}";
		def url = configuracaoDao.getProntoUrl() + "tickets/${ticketKey}"
		def msg = "${Seguranca.usuario} comenta\n\n ${comentario}\n\n${url}"
		return this.enviarMensagem(sbj, msg, to)
	}

	void enviarMensagem(String subject, String msg, def to) {
		mailMessageService.enviarMensagem(subject, msg, to);
		jabberMessageService.enviarMensagem(subject, msg, to);
	}
}
