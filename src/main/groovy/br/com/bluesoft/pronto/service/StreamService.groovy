package br.com.bluesoft.pronto.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import br.com.bluesoft.pronto.dao.MovimentoKanbanDao
import br.com.bluesoft.pronto.dao.TicketDao
import br.com.bluesoft.pronto.model.MovimentoKanban
import br.com.bluesoft.pronto.model.TicketComentario
import br.com.bluesoft.pronto.to.StreamItem

@Service
class StreamService {

	@Autowired TicketDao ticketDao
	@Autowired  MovimentoKanbanDao movimentoKanbanDao
	
	List<StreamItem> listarStream() {
		def stream = []
		movimentoKanbanDao.listarUltimosMovimentos(25).each { MovimentoKanban mk ->
			stream << new StreamItem(usuario:mk.usuario, ticket:mk.ticket, mensagem:mk.descricao, data:mk.data)	
		}
		ticketDao.listarUltimosComentarios(25).each { TicketComentario tc ->
			stream << new StreamItem(usuario:tc.usuario, ticket:tc.ticket, mensagem:tc.html, data:tc.data)	
		}
		stream.sort { it.data }.reverse()
	}
	
	
}
