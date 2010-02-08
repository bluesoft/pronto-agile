package br.com.bluesoft.pronto.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import br.com.bluesoft.pronto.model.Classificacao;
import br.com.bluesoft.pronto.model.Sprint;
import br.com.bluesoft.pronto.model.Ticket;
import br.com.bluesoft.pronto.model.TicketOrdem;
import br.com.bluesoft.pronto.service.Seguranca;
import static org.springframework.web.bind.annotation.RequestMethod.*
import br.com.bluesoft.pronto.core.Backlog;
import br.com.bluesoft.pronto.core.Papel;
import br.com.bluesoft.pronto.core.TipoDeTicket;
import br.com.bluesoft.pronto.dao.BacklogDao;
import br.com.bluesoft.pronto.dao.ClienteDao;
import br.com.bluesoft.pronto.dao.KanbanStatusDao;
import br.com.bluesoft.pronto.dao.SprintDao;
import br.com.bluesoft.pronto.dao.TicketDao;
import br.com.bluesoft.pronto.dao.TipoDeTicketDao;
import br.com.bluesoft.pronto.dao.UsuarioDao;

@Controller
@RequestMapping("/backlogs")
class BacklogController {
	
	private static final String VIEW_LISTAR = "/ticket/ticket.listar.jsp"
	private static final String VIEW_LISTAR_AGRUPADO = "/ticket/ticket.listarAgrupado.jsp"
	private static final String VIEW_ESTIMAR = "/ticket/ticket.estimar.jsp"
		
	@Autowired ClienteDao clienteDao
	@Autowired SessionFactory sessionFactory
	@Autowired TicketDao ticketDao
	@Autowired SprintDao sprintDao
	@Autowired UsuarioDao usuarioDao
	@Autowired KanbanStatusDao kanbanStatusDao
	@Autowired TipoDeTicketDao tipoDeTicketDao
	@Autowired BacklogDao backlogDao
	
	
	@RequestMapping(value='/{backlogKey}', method=GET)
	String listarPorBacklog( Model model, @PathVariable  int backlogKey) {
		
		Seguranca.validarPermissao Papel.PRODUCT_OWNER, Papel.EQUIPE, Papel.SCRUM_MASTER
		
		def tickets = ticketDao.listarEstoriasEDefeitosPorBacklog(backlogKey)
		def tarefasSoltas = ticketDao.listarTarefasEmBacklogsDiferentesDasEstoriasPorBacklog(backlogKey)
		
		model.addAttribute "tickets", tickets
		model.addAttribute "tarefasSoltas", tarefasSoltas
		model.addAttribute "backlog", backlogDao.obter(backlogKey)
		
		def totaisPorTipoDeTicket = totaisPorTipoDeTicket(tickets, tarefasSoltas)
		model.addAttribute "descricaoTotal", montaDescricaoTotal(totaisPorTipoDeTicket)
		
		VIEW_LISTAR
	}
	
	@RequestMapping(value="/sprints/{sprintKey}", method=GET)
	String listarPorSprint( Model model,  @PathVariable int sprintKey) {
		
		Seguranca.validarPermissao Papel.PRODUCT_OWNER, Papel.EQUIPE, Papel.SCRUM_MASTER
		
		List<Ticket> tickets = ticketDao.listarEstoriasEDefeitosPorSprint(sprintKey)
		model.addAttribute "tickets", tickets
		model.addAttribute "sprint", sprintDao.obter(sprintKey)
		model.addAttribute "sprints", sprintDao.listarSprintsEmAberto()
		
		Map<Integer, Integer> totaisPorTipoDeTicket = totaisPorTipoDeTicket(tickets)
		model.addAttribute "descricaoTotal", montaDescricaoTotal(totaisPorTipoDeTicket)
		
		VIEW_LISTAR
	}
	
	@RequestMapping(value="/sprints/atual", method=GET)
	String sprintAtual( Model model) {
		Sprint sprint = sprintDao.getSprintAtual()
		int quantidadeDeSprints = sprintDao.listar().size()
		
		if (quantidadeDeSprints == 0) {
			model.addAttribute "mensagem", "Por favor, crie um sprint."
			return "forward:/app/sprints"
		} else if (sprint == null) {
			model.addAttribute "mensagem", "Por favor, informe qual é o Sprint atual."
			return "forward:/app/sprints"
		} else {
			return "redirect:/backlogs/sprints/${sprint.sprintKey}";
		}
	}
	
	@RequestMapping("/clientes")
	String listarTicketsPendentesPorCliente( Model model) {
		return listarTicketsPendentesPorCliente(model, null, -1, null, null)
		
	}
	
	@RequestMapping("/clientes/{clienteKey}")
	String listarTicketsPendentesPorCliente( Model model, @PathVariable Integer clienteKey, Integer kanbanStatusKey,    String ordem,  String classificacao) {
		
		Seguranca.validarPermissao Papel.PRODUCT_OWNER, Papel.EQUIPE, Papel.SCRUM_MASTER
		
		TicketOrdem ticketOrdem = TicketOrdem.PRIORIDADE_DO_CLIENTE
		
		if (ordem != null && ordem.length() > 0) {
			ticketOrdem = TicketOrdem.valueOf(ordem)
		}
		
		Classificacao ticketClassificacao = Classificacao.ASCENDENTE
		if (classificacao != null && classificacao.length() > 0) {
			ticketClassificacao = Classificacao.valueOf(classificacao)
		}
		
		List<Ticket> tickets = null
		tickets = ticketDao.buscar(null, kanbanStatusKey, clienteKey, ticketOrdem, ticketClassificacao)
		
		Multimap<String, Ticket> ticketsAgrupados = ArrayListMultimap.create()
		for ( Ticket ticket : tickets) {
			// Não exibe nem as tarefas nem os itens na lixeira
			if (ticket.getPai() == null && ticket.getBacklog().getBacklogKey() != Backlog.LIXEIRA) {
				ticketsAgrupados.put(ticket.getCliente() != null ? ticket.getCliente().getNome() : "Indefinido", ticket)
			}
		}
		
		model.addAttribute("ticketsAgrupados", ticketsAgrupados.asMap())
		
		ArrayList<String> grupo = new ArrayList<String>(ticketsAgrupados.keySet())
		Collections.sort(grupo)
		
		model.addAttribute("grupos", grupo)
		model.addAttribute("clienteKey", clienteKey)
		model.addAttribute("kanbanStatusKey", kanbanStatusKey)
		model.addAttribute("kanbanStatus", kanbanStatusDao.listar())
		model.addAttribute("clientes", clienteDao.listar())
		model.addAttribute("ordens", TicketOrdem.values())
		model.addAttribute("ordem", ticketOrdem)
		model.addAttribute("classificacoes", Classificacao.values())
		model.addAttribute("classificacao", ticketClassificacao)
		
		return VIEW_LISTAR_AGRUPADO
	}
	
	@RequestMapping("/{backlogKey}/estimar")
	String estimarBacklog( Model model, @PathVariable  int backlogKey) {
		Seguranca.validarPermissao(Papel.EQUIPE, Papel.PRODUCT_OWNER)
		List<Ticket> tickets = ticketDao.listarEstoriasEDefeitosPorBacklog(backlogKey)
		model.addAttribute("tickets", tickets)
		model.addAttribute("backlog", sessionFactory.getCurrentSession().get(Backlog.class, backlogKey))
		return VIEW_ESTIMAR
	}
		
	String montaDescricaoTotal( Map<Integer, Integer> totaisPorTipoDeTicket) {
		Integer totalDeDefeitos = totaisPorTipoDeTicket.get(TipoDeTicket.DEFEITO)
		Integer totalDeEstorias = totaisPorTipoDeTicket.get(TipoDeTicket.ESTORIA)
		Integer totalDeTarefas = totaisPorTipoDeTicket.get(TipoDeTicket.TAREFA)
		Integer totalDeIdeias = totaisPorTipoDeTicket.get(TipoDeTicket.IDEIA)
		
		StringBuilder descricaoTotal = new StringBuilder()
		descricaoTotal.append(totalDeDefeitos > 0 ? totalDeDefeitos + " defeito(s), " : "nenhum defeito, ")
		descricaoTotal.append(totalDeEstorias > 0 ? totalDeEstorias + " estória(s), " : "nenhuma estória, ")
		descricaoTotal.append(totalDeTarefas > 0 ? totalDeTarefas + " tarefa(s), " : "nenhuma tarefa, ")
		descricaoTotal.append(totalDeIdeias > 0 ? totalDeIdeias + " ideia(s)" : "nenhuma ideia")
		
		return descricaoTotal.toString()
	}
	
	Map<Integer, Integer> totaisPorTipoDeTicket( List<Ticket>... listas) {
		Map<Integer, Integer> totais = new HashMap<Integer, Integer>()
		totais.put(TipoDeTicket.DEFEITO, 0)
		totais.put(TipoDeTicket.ESTORIA, 0)
		totais.put(TipoDeTicket.TAREFA, 0)
		totais.put(TipoDeTicket.IDEIA, 0)
		
		for ( List<Ticket> tickets : listas) {
			for ( Ticket ticket : tickets) {
				totais.put(ticket.getTipoDeTicket().getTipoDeTicketKey(), totais.get(ticket.getTipoDeTicket().getTipoDeTicketKey()) + 1)
				for ( Ticket tarefa : ticket.getFilhos()) {
					if (tarefa.getBacklog().getBacklogKey() == ticket.getBacklog().getBacklogKey()) {
						totais.put(tarefa.getTipoDeTicket().getTipoDeTicketKey(), totais.get(tarefa.getTipoDeTicket().getTipoDeTicketKey()) + 1)
					}
				}
			}
		}
		
		return totais
	}
	
	
	
}
