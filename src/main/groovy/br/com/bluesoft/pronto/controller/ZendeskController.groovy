package br.com.bluesoft.pronto.controller;

import br.com.bluesoft.pronto.dao.ConfiguracaoDao;
import net.sf.json.JSONNull;
import org.springframework.ui.Model;
import br.com.bluesoft.pronto.dao.ClienteDao;
import br.com.bluesoft.pronto.dao.ConfiguracaoDao;
import br.com.bluesoft.pronto.dao.TicketDao;
import br.com.bluesoft.pronto.service.ZendeskService;
import org.springframework.beans.factory.annotation.Autowired;
import groovyx.net.http.HTTPBuilder;
import groovyx.net.http.RESTClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.*

@Controller
@RequestMapping("/zendesk")
class ZendeskController {
		
	@Autowired
	private ConfiguracaoDao configuracaoDao
	
	@Autowired
	private ClienteDao clienteDao
	
	@Autowired 
	private TicketDao ticketDao
	
	@Autowired
	private ZendeskService zendeskService
	
	@RequestMapping(value='/tickets/{zendeskTicketKey}', method=[GET])
	String tickets(Model model, @PathVariable int zendeskTicketKey) {
		
		if (!configuracaoDao.isZendeskAtivo()) {
			model.addAttribute 'mensagem', 'A integração do Pronto com o Zendesk está desabilitada!'
			return "/branca.jsp"
		}
		
		def ticketKey = ticketDao.obterTicketKeyIntegradoComZendesk(zendeskTicketKey)
		if (ticketKey) {
			return "redirect:/tickets/${ticketKey}"	
		}
		
		def zendeskTicket = zendeskService.obterTicket(zendeskTicketKey)
		model.addAttribute 'zendeskTicket', zendeskTicket

		def zendeskSolicitador = zendeskService.obterUsuario(zendeskTicket.requester_id)
		model.addAttribute 'zendeskSolicitador', zendeskSolicitador
		
		if (!(zendeskTicket.organization_id instanceof JSONNull)) {
			def zendeskCliente = zendeskService.obterCliente(zendeskTicket.organization_id)
			model.addAttribute 'zendeskCliente', zendeskCliente
		}
		
		model.addAttribute 'clientes', clienteDao.listar()
		
		return '/zendesk/zendesk.incluir.jsp'
				
	}
	
	
	@RequestMapping(value='/tickets', method=[POST])
	String incluir(Model model, int zendeskTicketKey, int tipoDeTicketKey, int clienteKey) {
		
		def ticketKey = ticketDao.obterTicketKeyIntegradoComZendesk(zendeskTicketKey)
		if (ticketKey) {
			return "redirect:/tickets/${ticketKey}"
		}
		
		def ticket = zendeskService.criarNovoTicketNoPronto(zendeskTicketKey, tipoDeTicketKey, clienteKey)
		zendeskService.incluirComentarioPrivado zendeskTicketKey, 'Ticket integrado com o Pronto.\r\n\r\nPara visualizá-lo acesse: ' + configuracaoDao.getProntoUrl() + 'tickets/' + ticket.ticketKey
		
		return "redirect:/tickets/${ticket.ticketKey}"
		
	}
	
	@RequestMapping(value='/tickets/{zendeskTicketKey}/comentarios', method=[POST])
	String incluirComentario(Model model, @PathVariable int zendeskTicketKey, String comentarioZendesk) {
		zendeskService.incluirComentarioPrivado zendeskTicketKey, comentarioZendesk
		def ticketKey = ticketDao.obterTicketKeyIntegradoComZendesk(zendeskTicketKey)
		return "redirect:/tickets/${ticketKey}"
	}
}
