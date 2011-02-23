/*
 * Copyright 2009 Pronto Agile Project Management.
 * This file is part of Pronto.
 * Pronto is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Pronto is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Pronto. If not, see <http://www.gnu.org/licenses/>.
 */

package br.com.bluesoft.pronto.controller

import org.springframework.stereotype.Controller 
import org.springframework.web.bind.annotation.RequestMapping
import static org.springframework.web.bind.annotation.RequestMethod.*
import org.springframework.stereotype.Controller 
import org.springframework.web.bind.annotation.RequestMapping
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.util.List
import java.util.Map;

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.commons.fileupload.FileItem
import org.apache.commons.fileupload.FileItemFactory
import org.apache.commons.fileupload.FileUploadException
import org.apache.commons.fileupload.disk.DiskFileItemFactory
import org.apache.commons.fileupload.servlet.ServletFileUpload
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import br.com.bluesoft.pronto.ProntoException
import br.com.bluesoft.pronto.core.Backlog;
import br.com.bluesoft.pronto.core.Papel;
import br.com.bluesoft.pronto.dao.ConfiguracaoDao;
import br.com.bluesoft.pronto.dao.SprintDao
import br.com.bluesoft.pronto.dao.TicketDao
import br.com.bluesoft.pronto.model.Sprint
import br.com.bluesoft.pronto.model.Ticket
import br.com.bluesoft.pronto.service.Config
import br.com.bluesoft.pronto.service.Seguranca;
import br.com.bluesoft.pronto.web.binding.DefaultBindingInitializer;

import java.io.IOException
import org.apache.commons.fileupload.FileUploadException

@Controller
@RequestMapping("/sprints")
class SprintController {
	
	private static final String VIEW_LISTAR = "/sprint/sprint.listar.jsp"
	private static final String VIEW_EDITAR = "/sprint/sprint.editar.jsp"
	public static final String VIEW_ESTIMAR = "/ticket/ticket.estimar.jsp"
	private static final String VIEW_PRIORIZAR = "/ticket/ticket.priorizar.jsp"

	@Autowired SessionFactory sessionFactory
	@Autowired SprintDao sprintDao
	@Autowired TicketDao ticketDao
	@Autowired ConfiguracaoDao configuracaoDao
	
	@InitBinder
	public void initBinder(final WebDataBinder binder, final WebRequest webRequest) {
		def defaultBindingInitializer = new DefaultBindingInitializer()
		defaultBindingInitializer.initBinder binder, webRequest
	}
	
	@RequestMapping(method = GET)
	String listar(final Model model) {
		"redirect:/sprints/pendentes"
	}
	
	@RequestMapping(value="todos", method = GET)
	String listarTodos(final Model model) {
		model.addAttribute("sprints", sprintDao.listar())
		model.addAttribute("status", "todos")
		return VIEW_LISTAR
	}
	
	@RequestMapping(value="pendentes", method = GET)
	String listarPendentes(final Model model) {
		model.addAttribute("sprints", sprintDao.listarSprintsEmAberto())
		model.addAttribute("status", "pendentes")
		return VIEW_LISTAR
	}
	
	@RequestMapping(value="fechados", method = GET)
	String listarFechados(final Model model) {
		model.addAttribute("sprints", sprintDao.listarSprintsFechados())
		model.addAttribute("status", "fechados")
		return VIEW_LISTAR
	}
	
	@RequestMapping(value = "{sprintKey}/mudarParaAtual", method = POST)
	public String atual(@PathVariable final int sprintKey) {
		
		final Transaction tx = sessionFactory.currentSession.beginTransaction()
		
		sessionFactory.currentSession.createQuery("update Sprint s set s.atual = false").executeUpdate()
		sessionFactory.currentSession.flush()
		
		final Sprint sprint = (Sprint) sessionFactory.currentSession.get(Sprint.class, sprintKey)
		sprint.setAtual(true)
		sprint.setFechado(false)
		sessionFactory.currentSession.update(sprint)
		sessionFactory.currentSession.flush()
		tx.commit()
		
		"redirect:/sprints"
	}
	
	@RequestMapping("/novo")
	String novo(final Model model) {
		model.addAttribute "sprint", new Sprint()
		VIEW_EDITAR
	}
	
	@RequestMapping("/{sprintKey}")
	String editar(final Model model, @PathVariable Integer sprintKey) {
		def sprint = (Sprint) sessionFactory.currentSession.get(Sprint.class, sprintKey)
		model.addAttribute("sprint", sprint)
		VIEW_EDITAR
	}
	
	@RequestMapping(method = [ PUT, POST ])
	String salvar(Model model, Sprint sprint) {
		
		if (sprint.dataFinal.before(sprint.dataInicial)) {
			model.addAttribute "sprintKey", sprint.sprintKey
			model.addAttribute "erro", "A data inicial deve ser menor que a final"
			return "forward:/app/sprints/" + sprint.sprintKey
		}
		
		final Transaction tx = sessionFactory.getCurrentSession().beginTransaction()
		sessionFactory.getCurrentSession().saveOrUpdate(sprint)
		sessionFactory.getCurrentSession().flush()
		tx.commit()
		
		"redirect:/sprints"
	}
	
	@RequestMapping("/{sprintKey}/upload")
	String upload(final HttpServletRequest request, @PathVariable final int sprintKey) throws Exception {
		
		final Transaction tx = sessionFactory.getCurrentSession().beginTransaction()
		final Sprint sprint = (Sprint) sessionFactory.getCurrentSession().get(Sprint.class, sprintKey)
		
		def bytes = getImageBytes(request)
		
		final String folderPath = Config.getImagesFolder() + "/sprints/"
		final File folder = new File(folderPath)
		folder.mkdirs()
		
		final File file = new File(folderPath + sprint.getSprintKey())
		final FileOutputStream outputStream = new FileOutputStream(file)
		outputStream.write(bytes)
		outputStream.flush()
		
		sessionFactory.getCurrentSession().saveOrUpdate(sprint)
		sessionFactory.getCurrentSession().flush()
		
		tx.commit()
		return "redirect:/sprints/${sprintKey}"
	}
	
	@RequestMapping("/{sprintKey}/imagem")
	public String imagem(final HttpServletResponse response, @PathVariable final int sprintKey) throws Exception {
		
		final String folderPath = Config.getImagesFolder() + "/sprints/"
		
		final File arquivo = new File(folderPath + sprintKey)
		
		final byte[] bytes
		if (arquivo.exists()) {
			final FileInputStream fis = new FileInputStream(arquivo)
			final int numberBytes = fis.available()
			bytes = new byte[numberBytes]
			fis.read(bytes)
			fis.close()
		} else {
			final InputStream resource = this.getClass().getResourceAsStream("/noimage.gif")
			final int numberBytes = resource.available()
			bytes = new byte[numberBytes]
			resource.read(bytes)
			resource.close()
		}
		
		response.getOutputStream().write(bytes)
		return null
		
	}
	
	private def getImageBytes(final HttpServletRequest request) throws FileUploadException, IOException {
		FileItemFactory factory = new DiskFileItemFactory()
		ServletFileUpload upload = new ServletFileUpload(factory)
		List<FileItem> items = upload.parseRequest(request)

		def bytes = []
		
		items.each() { item ->  
			final InputStream inputStream = ((FileItem) item).inputStream
			final int numberBytes = inputStream.available()
			bytes = new byte[numberBytes]
			inputStream.read(bytes)
		}
		return bytes
	}
	
	@RequestMapping("/{sprintKey}/fechar")
	public String fechar(final Model model, @PathVariable final int sprintKey) throws ProntoException {
		
		try {
			final Sprint sprintParaFechar = (Sprint) sessionFactory.getCurrentSession().get(Sprint.class, sprintKey)
			
			if (sprintParaFechar.isAtual()) {
				throw new ProntoException("Não é possível fechar o Sprint Atual!")
			}
			
			final Transaction tx = sessionFactory.getCurrentSession().beginTransaction()
			
			final List<Ticket> ticketsParaMover = ticketDao.listarNaoConcluidosPorSprint(sprintKey)
			final Sprint sprintAtual = sprintDao.getSprintAtual()
			ticketDao.moverParaSprintAtual(ticketsParaMover, sprintAtual)
			
			sprintParaFechar.setFechado(true)
			sprintDao.salvar(sprintParaFechar)
			tx.commit()
			
			return "redirect:/sprints"
		} catch (final Exception e) {
			model.addAttribute("erro", e.getMessage())
			return "forward:/sprints"
		}
		
	}
	
	@RequestMapping("/{sprintKey}/reabrir")
	public String reabrir(final Model model, @PathVariable final int sprintKey) throws ProntoException {
		try {
			final Sprint sprintParaReabrir = (Sprint) sessionFactory.getCurrentSession().get(Sprint.class, sprintKey)
			final Transaction tx = sessionFactory.getCurrentSession().beginTransaction()
			sprintParaReabrir.setFechado(false)
			sprintDao.salvar(sprintParaReabrir)
			tx.commit()
			return "redirect:/sprints"
		} catch (e) {
			model.addAttribute("erro", e.message)
			return "forward:/sprints"
		}
	}
	
	@RequestMapping(value="/{sprintKey}/estimar", method=GET)
	String estimarSprint( Model model,  @PathVariable int sprintKey) {
		
		Seguranca.validarPermissao Papel.EQUIPE, Papel.PRODUCT_OWNER
		
		List<Ticket> tickets = ticketDao.listarEstoriasEDefeitosPorSprint(sprintKey)
		model.addAttribute("tickets", tickets)
		model.addAttribute("sprint", sessionFactory.getCurrentSession().get(Sprint.class, sprintKey))
		model.addAttribute "configuracoes", configuracaoDao.getMapa()
		return VIEW_ESTIMAR
	}

	@RequestMapping(value="/{sprintKey}/priorizar", method=GET)
	String priorizarSprint( Model model, @PathVariable  int sprintKey) {
		
		Seguranca.validarPermissao Papel.PRODUCT_OWNER, Papel.ADMINISTRADOR
		
		def mapa = [:]
		
		def tickets = ticketDao.listarEstoriasEDefeitosPorSprint(sprintKey)
		tickets.each {
			if (mapa[it.valorDeNegocio] == null) {
				mapa[it.valorDeNegocio] = [] as List
			}
			mapa[it.valorDeNegocio].add(it)
		}
		
		model.addAttribute("mapa", mapa)
		model.addAttribute("valores", mapa.keySet())
		model.addAttribute("sprint", sessionFactory.getCurrentSession().get(Sprint.class, sprintKey))
		return VIEW_PRIORIZAR
		
	}
	
	@RequestMapping(value="/{sprintKey}/priorizar", method=POST)
	@ResponseBody String priorizarSprint( Model model, @PathVariable int sprintKey, Integer[] ticketKey, Integer valor) {
		def tx = ticketDao.session.beginTransaction()
		ticketDao.priorizar(ticketKey, valor)
		tx.commit()
		"true"
	}
	

	@RequestMapping(value="/{sprintKey}/adicionarTarefas", method=GET)
	String listarTarefasParaAdicionarAoSprint( Model model, @PathVariable int sprintKey)  {
		
		Seguranca.validarPermissao Papel.PRODUCT_OWNER, Papel.ADMINISTRADOR
		
		model.addAttribute("sprint", sessionFactory.getCurrentSession().get(Sprint.class, sprintKey))
		model.addAttribute("tickets", ticketDao.listarEstoriasEDefeitosDoProductBacklog())
		return "/ticket/ticket.adicionarAoSprint.jsp"
	}
	
	@RequestMapping(value="/{sprintKey}/adicionarTarefas", method=POST)
	String adicionarAoSprint( Model model, @PathVariable int sprintKey,  int[] ticketKey) {
		
		Seguranca.validarPermissao Papel.PRODUCT_OWNER, Papel.ADMINISTRADOR
		
		Sprint sprint = (Sprint) sessionFactory.getCurrentSession().get(Sprint.class, sprintKey)
		for ( int key : ticketKey) {
			Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, key)
			ticket.setBacklog((Backlog) sessionFactory.getCurrentSession().get(Backlog.class, Backlog.SPRINT_BACKLOG))
			ticket.setSprint(sprint)
			ticketDao.salvar(ticket)
		}
		return "redirect:/backlogs/sprints/${sprintKey}"
	}
	
	@RequestMapping(value="/{sprintKey}/backlog", method=GET)
	String backlog(Model model, @PathVariable int sprintKey) {
		"redirect:/backlogs/sprints/${sprintKey}"
	}
}
