package br.com.bluesoft.pronto.controller;

import br.com.bluesoft.pronto.dao.ConfiguracaoDao;
import org.springframework.web.bind.annotation.RequestMethod;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import net.sf.json.JSONNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.dao.ClienteDao;
import br.com.bluesoft.pronto.dao.ConfiguracaoDao;
import br.com.bluesoft.pronto.dao.ProjetoDao;
import br.com.bluesoft.pronto.dao.TicketDao;
import br.com.bluesoft.pronto.service.ZendeskService;

@Controller
@RequestMapping("/zendesk")
class ZendeskController {
		
	@Autowired	ConfiguracaoDao configuracaoDao
	@Autowired ClienteDao clienteDao
	@Autowired ProjetoDao projetoDao
	@Autowired TicketDao ticketDao
	@Autowired ZendeskService zendeskService
	
	@RequestMapping(value='/tickets/{zendeskTicketKey}', method=[RequestMethod.GET])
	String tickets(Model model, @PathVariable int zendeskTicketKey) {
		
		if (!configuracaoDao.isZendeskAtivo()) {
			model.addAttribute 'mensagem', 'A integração do Pronto com o Zendesk está desabilitada!'
			return "/branca.jsp"
		}
		
		def tickets = ticketDao.obterTicketsIntegradoComZendesk(zendeskTicketKey)
		if (tickets && tickets.size() > 0) {
			if (tickets.size() == 1) {
				def ticketKey = tickets.iterator().next()
				return "redirect:/tickets/${ticketKey}"
			} else {
				return "redirect:/buscar?zendeskTicket=${zendeskTicketKey}"
			}
		} 
		
		def zendeskTicket = zendeskService.obterTicket(zendeskTicketKey)
		if (!zendeskTicket) {
			model.addAttribute 'mensagem', "O ticket #${zendeskTicketKey} não foi localizado no Zendesk."
			return "/branca.jsp"
		}
			
		model.addAttribute 'zendeskTicket', zendeskTicket

		def zendeskSolicitador = zendeskService.obterUsuario(zendeskTicket.requester_id)
		model.addAttribute 'zendeskSolicitador', zendeskSolicitador
		
		if (!(zendeskTicket.organization_id instanceof JSONNull)) {
			def zendeskCliente = zendeskService.obterCliente(zendeskTicket.organization_id)
			model.addAttribute 'zendeskCliente', zendeskCliente
		}
		
		model.addAttribute 'clientes', clienteDao.listar()
		model.addAttribute 'projetos', projetoDao.listar()
		
		return '/zendesk/zendesk.incluir.jsp'
				
	}
	
	
	@RequestMapping(value='/tickets', method=[RequestMethod.POST])
	String incluir(Model model, int zendeskTicketKey, int tipoDeTicketKey, int clienteKey, int projetoKey) {
		
		def ticketKey = ticketDao.obterTicketKeyIntegradoComZendesk(zendeskTicketKey)
		if (ticketKey) {
			return "redirect:/tickets/${ticketKey}"
		}
		
		def ticket = zendeskService.criarNovoTicketNoPronto(zendeskTicketKey, tipoDeTicketKey, clienteKey, projetoKey)
		zendeskService.incluirComentarioPrivado zendeskTicketKey, 'Ticket integrado com o Pronto.\r\n\r\nPara visualizá-lo acesse: ' + configuracaoDao.getProntoUrl() + 'tickets/' + ticket.ticketKey
		zendeskService.notificarInclusao zendeskTicketKey
		
		return "redirect:/tickets/${ticket.ticketKey}"
		
	}
	
	@RequestMapping(value='/tickets/{zendeskTicketKey}/comentarios', method=[RequestMethod.POST])
	String incluirComentario(Model model, @PathVariable int zendeskTicketKey, String comentarioZendesk, int ticketKey) {
		zendeskService.incluirComentarioPrivado zendeskTicketKey, comentarioZendesk
		return "redirect:/tickets/${ticketKey}"
	}
}
