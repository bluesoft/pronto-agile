package br.com.bluesoft.pronto.service

import br.com.bluesoft.pronto.dao.ConfiguracaoDao;

import org.springframework.beans.factory.annotation.Autowired;
import br.com.bluesoft.pronto.model.Usuario;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.jivesoftware.smack.Chat 
import org.jivesoftware.smack.ChatManager 
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener 
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message 

@Service
class JabberMessageService {
	
	@Autowired ConfiguracaoDao configuracaoDao
	
	XMPPConnection connection 
	MessageListener listener
	
	@PostConstruct
	void init(){
		listener = new MessageListener() {
			void processMessage(Chat chat, Message message) {
				chat.sendMessage('Ei, não fale comigo!')
			}
		}
	}
	
	boolean send(String msg, def to) {
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
			return false
		}
	}
	
	void connect() {
		if (connection == null || !connection.isConnected()) {
			ConnectionConfiguration config = new ConnectionConfiguration(configuracaoDao.getJabberUrl(), 5222);
			this.connection = new XMPPConnection(config);
			this.connection.connect()
			this.connection.login(configuracaoDao.getJabberUserName(), configuracaoDao.getJabberPassword())
		}
	}
	
	@PreDestroy
	void destroy() {
		if (connection != null && connection.isConnected()) {
			this.connection.disconnect()
		}
	}
	
}
