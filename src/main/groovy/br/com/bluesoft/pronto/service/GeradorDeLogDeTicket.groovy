package br.com.bluesoft.pronto.service

import br.com.bluesoft.pronto.annotations.Auditable;
import br.com.bluesoft.pronto.annotations.Label;
import br.com.bluesoft.pronto.model.TicketLog;
import br.com.bluesoft.pronto.util.DateUtil;

class GeradorDeLogDeTicket {
	
	static def gerarLogs(def velho, def novo) {
		
		def logs = [];
		
		def properties = velho.getClass().getDeclaredFields() as List
		
		properties.each {
			def name = it.name
			
			boolean isAuditable = it.isAnnotationPresent(Auditable.class);
			def valorVelho = preparaValor(velho."${name}")
			def valorNovo = preparaValor(novo."${name}")
			
			if (isAuditable && valorVelho != valorNovo) {
				def ticketLog = new TicketLog()
				ticketLog.campo = getDescricaoDoCampo(it)
				ticketLog.data = new Date()
				ticketLog.usuario = Seguranca.usuario.username
				ticketLog.valorAntigo = valorVelho
				ticketLog.valorNovo = valorNovo
				ticketLog.operacao = TicketLog.ALTERACAO
				ticketLog.ticket = novo
				logs.add ticketLog
			}
			
		}
		
		return logs
		
	}
	
	private static String getDescricaoDoCampo(def field) {
		final boolean temLabel = field.isAnnotationPresent(Label.class);
		if (temLabel) {
			return field.getAnnotation(Label.class).value()
		} else {
			return field.name	
		}
		
	}
	
	private static String preparaValor(Object valor) {
		
		String str = String.valueOf(valor);
		
		if (valor instanceof Date) {
			str = DateUtil.toString((Date) valor);
		}
		
		if (valor == null || valor.equals("null")) {
			return "em branco";
		} else {
			return str;
		}
	}
	
}
