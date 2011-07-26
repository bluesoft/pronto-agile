package br.com.bluesoft.pronto.service;

import org.springframework.scheduling.annotation.Async;

interface MessageService {

	@Async
	void enviarMensagem(String subject, String msg, def to);
}
