package br.com.bluesoft.pronto.service

import javax.annotation.PostConstruct;

import groovyx.net.http.HTTPBuilder;
import groovyx.net.http.RESTClient;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.JavaScriptUtils;

import br.com.bluesoft.pronto.core.Backlog;
import br.com.bluesoft.pronto.core.KanbanStatus;
import br.com.bluesoft.pronto.core.TipoDeTicket;
import br.com.bluesoft.pronto.dao.BacklogDao;
import br.com.bluesoft.pronto.dao.ClienteDao;
import br.com.bluesoft.pronto.dao.ConfiguracaoDao;
import br.com.bluesoft.pronto.dao.KanbanStatusDao;
import br.com.bluesoft.pronto.dao.TicketDao;
import br.com.bluesoft.pronto.dao.TipoDeTicketDao;
import br.com.bluesoft.pronto.model.Ticket;
import br.com.bluesoft.pronto.util.MD5Util;
import static groovyx.net.http.ContentType.*;
import static groovyx.net.http.Method.*;


@Service
class ZendeskService {
	
	def mapStatus = ['0':'Novo','1':'Aberto','2':'Pendente','3':'Resolvido','4':'Fechado'] 
	def mapTipo = ['0':'Indefinido','1':'Pergunta','2':'Incidente','3':'Problema','4':'Tarefa']
	
	@Autowired 
	ConfiguracaoDao configuracaoDao
	
	@Autowired 
	TipoDeTicketDao tipoDeTicketDao
	
	@Autowired 
	ClienteDao clienteDao
	
	@Autowired 
	KanbanStatusDao kanbanStatusDao
	
	@Autowired
	BacklogDao backlogDao  
	
	@Autowired
	TicketDao ticketDao
	
	public RESTClient getRESTClient() {
		RESTClient rest = new RESTClient(configuracaoDao.getZendeskUrl())
		rest.auth.basic configuracaoDao.getZendeskUserName(), configuracaoDao.getZendeskPassword()
		return rest
	}
	
	
	def obterTicket(int zendeskTicketKey){
		def cache = CacheManager.getInstance().getCache("zendeskTickets")
		def ticket = cache.get(zendeskTicketKey)
		if (ticket) {
			return ticket.value
		} else {
			ticket = getRESTClient().get( path : "/tickets/${zendeskTicketKey}.json" ).data
			ticket.html = String.valueOf(ticket.description).replaceAll("(\r\n)|(\n\n)","<br/>")
			ticket.status = mapStatus[ticket.status_id as String]
			ticket.tipo = mapTipo[ticket.ticket_type_id  as String]
			ticket.comments.each {
				it.author = this.obterUsuario(it.author_id)
				it.html = String.valueOf(it.value).replaceAll("(\r\n)|(\n\n)","<br/>")
			}
			cache.put new Element(zendeskTicketKey, ticket)
			return ticket
		}
	}
	
	def obterCliente(int clienteKey){
		def cache = CacheManager.getInstance().getCache("zendeskOrganizations")
		def cliente = cache.get(clienteKey) 
		if (cliente) {
			return cliente.value
		} else {
			cliente = getRESTClient().get( path : "/organizations/${clienteKey}.json" ).data
			cache.put new Element(clienteKey, cliente)
			return cliente
		}
	}
	
	def obterUsuario(int usuarioKey){
		def cache = CacheManager.getInstance().getCache("zendeskUsers")
		def usuario = cache.get(usuarioKey)
		if (usuario) {
			return usuario.value
		} else {
			usuario = getRESTClient().get( path : "/users/${usuarioKey}.json" ).data
			usuario.emailMD5 = MD5Util.md5Hex(usuario.email)
			cache.put new Element(usuarioKey, usuario)
			return usuario
		}
	}
	
	def criarNovoTicketNoPronto(int zendeskTicketKey, int tipoDeTicketKey, int clienteKey) {
		
		def zendeskTicket = obterTicket(zendeskTicketKey)
		def zendeskSolicitador = obterUsuario(zendeskTicket.requester_id)
		
		def ticket = new Ticket()
		ticket.sprint = null 
		ticket.cliente = clienteDao.obter(clienteKey)
		ticket.kanbanStatus = kanbanStatusDao.obter(KanbanStatus.TO_DO)
		ticket.tipoDeTicket = tipoDeTicketDao.obter(tipoDeTicketKey)
		ticket.backlog = tipoDeTicketKey == TipoDeTicket.DEFEITO ? backlogDao.obter(Backlog.PRODUCT_BACKLOG) : backlogDao.obter(Backlog.IDEIAS)
		ticket.titulo = zendeskTicket.subject
		ticket.descricao = zendeskTicket.description
		ticket.solicitador = zendeskSolicitador.name
		ticket.reporter = Seguranca.getUsuario()
		ticketDao.salvar ticket
		ticketDao.relacionarComZendesk ticket.ticketKey, zendeskTicket.nice_id
		return ticket
	}
	
	def incluirComentarioPublico(int zendeskTicketKey, String comentario) {
		this.incluirComentario zendeskTicketKey, comentario, true
	}
	
	def incluirComentarioPrivado(int zendeskTicketKey, String comentario) {
		this.incluirComentario zendeskTicketKey, comentario, false
	}
	
	private def incluirComentario(int zendeskTicketKey, String comentario, boolean publico) {
		comentario = HtmlUtils.htmlEscape(comentario)
		def comment  = ['comment':['is_public':'${publico}', 'value':comentario]]
		def resp = getRESTClient().put(path:"/tickets/${zendeskTicketKey}.json",  contentType: TEXT, requestContentType: JSON,	body:comment)
		CacheManager.getInstance().getCache("zendeskTickets").removeQuiet(zendeskTicketKey)
	}
}
