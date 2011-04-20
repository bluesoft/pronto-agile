package br.com.bluesoft.pronto.controller

import java.util.Date
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

import com.google.common.collect.Maps;

import br.com.bluesoft.pronto.ProntoException;
import br.com.bluesoft.pronto.SegurancaException
import br.com.bluesoft.pronto.core.KanbanStatus
import br.com.bluesoft.pronto.core.Papel
import br.com.bluesoft.pronto.dao.KanbanStatusDao
import br.com.bluesoft.pronto.dao.MotivoReprovacaoDao
import br.com.bluesoft.pronto.dao.MovimentoKanbanDao
import br.com.bluesoft.pronto.dao.SprintDao
import br.com.bluesoft.pronto.dao.TicketDao
import br.com.bluesoft.pronto.model.MovimentoKanban;
import br.com.bluesoft.pronto.model.Sprint
import br.com.bluesoft.pronto.model.Ticket
import br.com.bluesoft.pronto.service.Seguranca
import br.com.bluesoft.pronto.service.MovimentadorDeTicket;
import static org.springframework.web.bind.annotation.RequestMethod.*

@Controller
@RequestMapping("/kanban")
public class KanbanController {
	
	private static final String VIEW_KANBAN = "/kanban/kanban.kanban.jsp"
	
	@Autowired SessionFactory sessionFactory
	@Autowired SprintDao sprintDao
	@Autowired TicketDao ticketDao
	@Autowired KanbanStatusDao kanbanStatusDao
	@Autowired MotivoReprovacaoDao motivoReprovacaoDao
	@Autowired MovimentoKanbanDao movimentoKanbanDao
	@Autowired MovimentadorDeTicket movimentadorDeTicket
	
	@RequestMapping(value= '/{sprintKey}', method = GET)
	String index(final Model model, @PathVariable int sprintKey) {
		index model, new Sprint(sprintKey: sprintKey)
	}
	
	@RequestMapping(value= '/atualizar/{sprintKey}', method = GET)
	@ResponseBody Map<Integer,Integer> atualizar(final Model model, @PathVariable String sprintKey) {
		Integer springKeyInt = NumberUtils.toInt(sprintKey)
		if (springKeyInt > 0) {
			return ticketDao.listarKanbanStatusDosTicketsDoSprint(springKeyInt)
		} else {
			return Maps.newHashMap()
		}
	}
	
	@RequestMapping(method = GET)
	String index(final Model model, Sprint sprint) {
		
		Seguranca.validarPermissao Papel.PRODUCT_OWNER, Papel.EQUIPE, Papel.SCRUM_MASTER
		
		if (sprint == null || sprint.sprintKey == 0) {
			sprint = sprintDao.getSprintAtual()
		}
		
		if (sprint == null) {
			return LoginController.VIEW_BEM_VINDO
		}

		sprint = sprintDao.obterSprintComTicket(sprint.getSprintKey())
		
		model.addAttribute "sprint", sprint
		
		def sprints = sprintDao.listarSprintsEmAberto()
		def sprintsPorProjeto = [:]
		sprints.each {
			if (!sprintsPorProjeto[it.projeto]) {
				sprintsPorProjeto[it.projeto] = []
			}
			sprintsPorProjeto[it.projeto] << it
		}
		
		model.addAttribute "projetos", sprintsPorProjeto.keySet()
		model.addAttribute "sprints", sprintsPorProjeto 
		
		def mapaDeTickets = sprint.ticketsParaOKanbanPorEtapa
		model.addAttribute 'mapaDeTickets', mapaDeTickets
		model.addAttribute 'mapaDeQuantidades', this.getMapaDeQuantidades(mapaDeTickets)
		model.addAttribute 'motivos', motivoReprovacaoDao.listar()

		def statusList = kanbanStatusDao.listarPorProjeto(sprint.projeto.projetoKey)
		model.addAttribute "status", statusList
		def ordens = new JSONObject();
		statusList.each {
			ordens.put it.kanbanStatusKey as String, it.ordem as String	
		}
		model.addAttribute "ordens", ordens
		
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
	@ResponseBody String mover(Model model, int ticketKey, int kanbanStatusKey, Integer motivoReprovacaoKey) throws SegurancaException {
	
		String mensagem = ""
			
		try {
			Seguranca.validarPermissao(Papel.EQUIPE)
			def ticket = ticketDao.obterComUsuariosEnvolvidos(ticketKey)
			movimentadorDeTicket.movimentar ticket, kanbanStatusKey, motivoReprovacaoKey
			mensagem = "true"
		} catch(ProntoException e) {
			mensagem = "${e.message}"	
		} catch(Exception e) {
			e.printStackTrace()
			mensagem = "Ocorreu um erro desconhecido!"
		} finally {
			return mensagem;
		}
	}
	
}
