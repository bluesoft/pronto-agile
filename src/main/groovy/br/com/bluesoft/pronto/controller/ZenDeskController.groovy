package br.com.bluesoft.pronto.controller;

import br.com.bluesoft.pronto.dao.ConfiguracaoDao;
import org.springframework.ui.Model;

import br.com.bluesoft.pronto.dao.ClienteDao;
import br.com.bluesoft.pronto.dao.ConfiguracaoDao;
import br.com.bluesoft.pronto.dao.TicketDao;
import br.com.bluesoft.pronto.service.ZenDeskService;

import org.springframework.beans.factory.annotation.Autowired;
import groovyx.net.http.HTTPBuilder;
import groovyx.net.http.RESTClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.*

@Controller
@RequestMapping("/zendesk")
class ZenDeskController {
		
	@Autowired
	private ConfiguracaoDao configuracaoDao
	
	@Autowired
	private ClienteDao clienteDao
	
	@Autowired 
	private TicketDao ticketDao
	
	@Autowired
	private ZenDeskService zenDeskService
	
	@RequestMapping(value='/tickets/{zenDeskTicketKey}', method=[GET])
	String tickets(Model model, @PathVariable int zenDeskTicketKey) {
		
		if (!configuracaoDao.isZenDeskAtivo()) {
			model.addAttribute 'mensagem', 'A integração do pronto com ZenDesk está desabilitada!'
			return "/branca.jsp"
		}
		
		def ticketKey = ticketDao.obterTicketKeyIntegradoComZenDesk(zenDeskTicketKey)
		if (ticketKey) {
			return "redirect:/tickets/${ticketKey}"	
		}
		
		def zenDeskTicket = zenDeskService.obterTicket(zenDeskTicketKey)
		def zenDeskCliente = zenDeskService.obterCliente(zenDeskTicket.organization_id)
		def zenDeskSolicitador = zenDeskService.obterUsuario(zenDeskTicket.requester_id)
		
		model.addAttribute 'zenDeskTicket', zenDeskTicket
		model.addAttribute 'zenDeskCliente', zenDeskCliente
		model.addAttribute 'zenDeskSolicitador', zenDeskSolicitador
		model.addAttribute 'clientes', clienteDao.listar()
		
		return '/zenDesk/zenDesk.incluir.jsp'
				
	}
	
	
	@RequestMapping(value='/tickets', method=[POST])
	String incluir(Model model, int zenDeskTicketKey, int tipoDeTicketKey, int clienteKey) {
		
		def ticketKey = ticketDao.obterTicketKeyIntegradoComZenDesk(zenDeskTicketKey)
		if (ticketKey) {
			return "redirect:/tickets/${ticketKey}"
		}
		
		def ticket = zenDeskService.criarNovoTicketNoPronto(zenDeskTicketKey, tipoDeTicketKey, clienteKey)
		
		return "redirect:/tickets/${ticket.ticketKey}"
		
	}
	
	@RequestMapping(value='/tickets/{zenDeskTicketKey}/comentarios', method=[POST])
	String incluirComentario(Model model, @PathVariable int zenDeskTicketKey, String comentarioZenDesk) {
		zenDeskService.incluirComentarioPrivado zenDeskTicketKey, comentarioZenDesk
		def ticketKey = ticketDao.obterTicketKeyIntegradoComZenDesk(zenDeskTicketKey)
		return "redirect:/tickets/${ticketKey}"
	}
}
