package br.com.bluesoft.pronto.service

import org.hibernate.Query
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.packet.Message
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import br.com.bluesoft.pronto.dao.TicketDao
import br.com.bluesoft.pronto.dao.UsuarioDao
import br.com.bluesoft.pronto.model.Ticket
import br.com.bluesoft.pronto.model.TicketComentario
import br.com.bluesoft.pronto.model.Usuario


@Service
class ProntoMessageListener implements MessageListener {

	@Autowired SessionFactory sessionFactory
	
	public void processMessage(Chat chat, Message message) {
		String body = message.body
		if (body) {
			String ticketKeyStr = body.replaceAll('#(.*?) (.*)', '$1').trim()
			String mensagem = body.replaceAll('#(.*?) (.*)', '$2').trim()
			Session session = null
			try {
				session = sessionFactory.openSession()
				Integer ticketKey = Integer.parseInt(ticketKeyStr)
				Usuario user = findUser(chat.getParticipant(), session)
				if (user) {
					try {
						incluirComentario(ticketKey, user, mensagem, session)
						chat.sendMessage("Maravilha! Seu comentário foi incluído no ticket #${ticketKey}")
					} catch(e) {
						chat.sendMessage("Sinto muito, mas não foi impossível incluir seu comentário no ticket #${ticketKey}")
					}
				} else {
					chat.sendMessage("Usuário ${chat.participant} não localizado.")
				}
			} catch(e) {
				chat.sendMessage('Não entendi o que você disse.')
			} finally {
				if (session) {
					session.close()
				}
			}
		}
	}
	
	def findUser(String participant, session) {
		if (participant.indexOf('/') > 0) {
			participant = participant.replaceAll('(.*)/.*', '$1')
		}
		return obterPorJabber(participant, session)
	}
	
	Usuario obterPorJabber(String jabberUsername, Session session) {
		final String hql = "select distinct u from Usuario u where u.jabberUsername = :jabberUsername"
		final Query query = session.createQuery(hql).setString("jabberUsername", jabberUsername)
		return query.list()[0]
	}
	
	void incluirComentario(Integer ticketKey, Usuario usuario, String texto, Session session) {
		final TicketComentario comentario = new TicketComentario()
		comentario.setTicket(session.get(Ticket.class, ticketKey))
		comentario.setData(new Date())
		comentario.setUsuario(usuario)
		comentario.setTexto(texto)
		session.save(comentario)
		session.flush()
	}
}
