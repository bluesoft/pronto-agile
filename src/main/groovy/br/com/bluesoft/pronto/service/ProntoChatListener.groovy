package br.com.bluesoft.pronto.service

import org.jivesoftware.smack.ChatManagerListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class ProntoChatListener implements ChatManagerListener {

	@Autowired ProntoMessageListener prontoMessageListener
	
	void chatCreated(org.jivesoftware.smack.Chat chat, boolean createdLocally) {
		if (!createdLocally){
			chat.addMessageListener prontoMessageListener
		}
	}
}
