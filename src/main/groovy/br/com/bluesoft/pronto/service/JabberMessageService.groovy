package br.com.bluesoft.pronto.service

import java.awt.Color;

import br.com.bluesoft.pronto.dao.ConfiguracaoDao;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.bluesoft.pronto.model.MovimentoKanban;
import br.com.bluesoft.pronto.model.Usuario;
import br.com.bluesoft.pronto.web.servlet.StartupListener;

import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.jivesoftware.smack.Chat 
import org.jivesoftware.smack.ChatManager 
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener 
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message 
import org.jivesoftware.smackx.XHTMLText;

@Service
class JabberMessageService {
	
	@Autowired ConfiguracaoDao configuracaoDao
	
	XMPPConnection connection 
	MessageListener listener
	
	@PostConstruct
	void init(){
		listener = new MessageListener() {
					void processMessage(Chat chat, Message message) {
					}
				}
	}
	
	boolean enviarComentario(int ticketKey, String comentario, def to) {
		if (configuracaoDao.isJabberAtivo()) {
			def url = configuracaoDao.getProntoUrl() + "tickets/${ticketKey}"
			def msg = "${Seguranca.usuario} incluiu um comentário no ticket #${ticketKey}: ${comentario} - ${url}"
			return this.enviarMensagem(msg, to)
		}
		return false
	}
	
	boolean notificarMovimentacao(MovimentoKanban movimento) {
		if (configuracaoDao.isJabberAtivo()) {
			def url = configuracaoDao.getProntoUrl() + "tickets/${movimento.ticket.ticketKey}"
			def msg = "${Seguranca.usuario} moveu o ticket #${movimento.ticket.ticketKey} para ${movimento.kanbanStatus.descricao} - ${url}"
			return this.enviarMensagem(msg, movimento.getTicket().getEnvolvidosComReporter() - Seguranca.getUsuario())
		}
		return false
	}
	
	boolean enviarMensagem(String msg, def to) {
		if (configuracaoDao.isJabberAtivo()) {
			try {
				this.connect()
				ChatManager chatmanager = connection.getChatManager();
				to.each { Usuario usuario ->
					if (usuario.hasJabber()) {
						Chat newChat = chatmanager.createChat(usuario.jabberUsername, listener)
						newChat.sendMessage(msg);
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
	
	void connect() {
		if (configuracaoDao.isJabberAtivo()) {
			if (connection == null || !connection.isConnected()) {
				ConnectionConfiguration config = new ConnectionConfiguration(configuracaoDao.getJabberUrl(), 5222);
				config.setCompressionEnabled(true);
				config.setSASLAuthenticationEnabled(true);
				this.connection = new XMPPConnection(config);
				this.connection.connect()
				this.connection.login(configuracaoDao.getJabberUserName(), configuracaoDao.getJabberPassword())
			}
		}
	}
	
	@PreDestroy
	void destroy() {
		if (connection != null && connection.isConnected()) {
			this.connection.disconnect()
		}
	}
}
