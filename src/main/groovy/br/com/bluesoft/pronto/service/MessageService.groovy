package br.com.bluesoft.pronto.service;

interface MessageService {

	boolean enviarMensagem(String subject, String msg, def to);
}
