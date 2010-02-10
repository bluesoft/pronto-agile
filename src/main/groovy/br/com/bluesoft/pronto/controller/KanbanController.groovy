package br.com.bluesoft.pronto.controller

import java.util.Date
import java.util.Map.Entry;

import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping

import br.com.bluesoft.pronto.SegurancaException
import br.com.bluesoft.pronto.core.KanbanStatus
import br.com.bluesoft.pronto.core.Papel
import br.com.bluesoft.pronto.dao.KanbanStatusDao
import br.com.bluesoft.pronto.dao.SprintDao
import br.com.bluesoft.pronto.dao.TicketDao
import br.com.bluesoft.pronto.model.Sprint
import br.com.bluesoft.pronto.model.Ticket
import br.com.bluesoft.pronto.service.Seguranca
import static org.springframework.web.bind.annotation.RequestMethod.*

@Controller
@RequestMapping("/kanban")
public class KanbanController {
	
	private static final String VIEW_KANBAN = "/kanban/kanban.kanban.jsp"
	
	@Autowired SessionFactory sessionFactory
	
	@Autowired SprintDao sprintDao
	
	@Autowired TicketDao ticketDao
	
	@Autowired KanbanStatusDao kanbanStatusDao
	
	
	@RequestMapping(value= '/{sprintKey}', method = GET)
	String index(final Model model, @PathVariable int sprintKey) {
		index model, new Sprint(sprintKey: sprintKey)
	}
	
	@RequestMapping(method = GET)
	String index(final Model model, Sprint sprint) {
		
		Seguranca.validarPermissao Papel.PRODUCT_OWNER, Papel.EQUIPE, Papel.SCRUM_MASTER
		
		if (sprint == null || sprint.sprintKey == 0) {
			sprint = sprintDao.getSprintAtualComTickets()
		} else {
			sprint = sprintDao.obterSprintComTicket(sprint.getSprintKey())
		}
		
		if (sprint == null) {
			return LoginController.VIEW_BEM_VINDO
		}
		
		model.addAttribute "sprint", sprint
		model.addAttribute "status", kanbanStatusDao.listar()
		model.addAttribute "sprints", sprintDao.listarSprintsEmAberto()
		
		def mapaDeTickets = sprint.ticketsParaOKanbanPorEtapa
		model.addAttribute 'mapaDeTickets', mapaDeTickets
		model.addAttribute 'mapaDeQuantidades', this.getMapaDeQuantidades(mapaDeTickets);
		
		VIEW_KANBAN
	}
	
	private def getMapaDeQuantidades(def mapaDeTickets) {
		def mapaDeQuantidades = [:]
		mapaDeTickets.each { 
			mapaDeQuantidades[it.key] = it.value.size()	
		}
		return mapaDeQuantidades;
	}
	
	@RequestMapping("/mover")
	void mover(final Model model, final int ticketKey, final int kanbanStatusKey) throws SegurancaException {
		
		Seguranca.validarPermissao(Papel.EQUIPE)
		
		def ticket = (Ticket) sessionFactory.currentSession.get(Ticket.class, ticketKey)
		ticket.kanbanStatus = sessionFactory.currentSession.get(KanbanStatus.class, kanbanStatusKey)
		
		if (kanbanStatusKey == KanbanStatus.DONE) {
			ticket.setDataDePronto(new Date())
		} else {
			ticket.setDataDePronto(null)
		}
		
		ticketDao.salvar(ticket)
		
	}
}
