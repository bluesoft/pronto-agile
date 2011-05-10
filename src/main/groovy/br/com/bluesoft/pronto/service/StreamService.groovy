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
	
	static final int QUANTIDADE = 30
	
	List<StreamItem> listarStream() {
		def stream = []
		movimentoKanbanDao.listarUltimosMovimentos(QUANTIDADE).each { MovimentoKanban mk ->
			stream << new StreamItem(usuario:mk.usuario, ticket:mk.ticket, mensagem:mk.descricao, data:mk.data)	
		}
		ticketDao.listarUltimosComentarios(QUANTIDADE).each { TicketComentario tc ->
			stream << new StreamItem(usuario:tc.usuario, ticket:tc.ticket, mensagem:tc.html, data:tc.data)	
		}
		stream.sort { it.data }.reverse()
	}
	
	List<StreamItem> listarStream(String username) {
		def stream = []
		movimentoKanbanDao.listarUltimosMovimentos(QUANTIDADE, username).each { MovimentoKanban mk ->
			stream << new StreamItem(usuario:mk.usuario, ticket:mk.ticket, mensagem:mk.descricao, data:mk.data)
		}
		ticketDao.listarUltimosComentarios(QUANTIDADE, username).each { TicketComentario tc ->
			stream << new StreamItem(usuario:tc.usuario, ticket:tc.ticket, mensagem:tc.html, data:tc.data)
		}
		stream.sort { it.data }.reverse()
	}
	
	
}
