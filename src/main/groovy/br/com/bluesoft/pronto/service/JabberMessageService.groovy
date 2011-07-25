package br.com.bluesoft.pronto.service

import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.ChatManager
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.XMPPConnection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service

import br.com.bluesoft.pronto.dao.ConfiguracaoDao
import br.com.bluesoft.pronto.model.Usuario


@Service
class JabberMessageService implements MessageService {

	@Autowired ConfiguracaoDao configuracaoDao
	@Autowired ProntoMessageListener prontoMessageListener
	@Autowired ProntoChatListener prontoChatListener

	XMPPConnection connection

	@PostConstruct
	private void init() {
		try {
			connect()
		} catch(java.lang.RuntimeException e) {

		}
	}

	boolean enviarComentario(int ticketKey, String comentario, def to) {
		if (configuracaoDao.isJabberAtivo()) {
			def url = configuracaoDao.getProntoUrl() + "tickets/${ticketKey}"
			def msg = "<${Seguranca.usuario}>: ${comentario} \n\n${url}"
			return this.enviarMensagem(msg, to)
		}
		return false
	}


	@Async
	public boolean enviarMensagem(String subject, String msg, def to) {
		if (configuracaoDao.isJabberAtivo()) {
			try {
				this.connect()
				ChatManager chatmanager = connection.getChatManager();
				to.each { Usuario usuario ->
					if (usuario.hasJabber()) {
						Chat newChat = chatmanager.createChat(usuario.jabberUsername, prontoMessageListener)
						newChat.sendMessage(subject + "\n\n" + msg);
					}
				}
				return true
			} catch(Throwable e) {
				e.printStackTrace()
				return false
			}
		}
		return false
	}

	private void connect() {
		if (configuracaoDao.isJabberAtivo()) {
			if (connection == null || !connection.isConnected()) {
				ConnectionConfiguration config = new ConnectionConfiguration(configuracaoDao.getJabberUrl(), 5222);
				config.setCompressionEnabled(true);
				config.setSASLAuthenticationEnabled(true);
				this.connection = new XMPPConnection(config);
				this.connection.connect()
				this.connection.login(configuracaoDao.getJabberUserName(), configuracaoDao.getJabberPassword())
				connection.getChatManager().addChatListener(prontoChatListener)
			}
		}
	}

	@PreDestroy
	private void destroy() {
		if (connection != null && connection.isConnected()) {
			this.connection.disconnect()
		}
	}
}
