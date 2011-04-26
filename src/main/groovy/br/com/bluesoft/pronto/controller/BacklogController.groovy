package br.com.bluesoft.pronto.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*

import java.util.List
import java.util.Map

import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

import br.com.bluesoft.pronto.core.Backlog
import br.com.bluesoft.pronto.core.Papel
import br.com.bluesoft.pronto.core.TipoDeTicket
import br.com.bluesoft.pronto.dao.BacklogDao
import br.com.bluesoft.pronto.dao.CategoriaDao
import br.com.bluesoft.pronto.dao.ClienteDao
import br.com.bluesoft.pronto.dao.ConfiguracaoDao
import br.com.bluesoft.pronto.dao.KanbanStatusDao
import br.com.bluesoft.pronto.dao.ProjetoDao
import br.com.bluesoft.pronto.dao.SprintDao
import br.com.bluesoft.pronto.dao.TicketDao
import br.com.bluesoft.pronto.dao.TipoDeTicketDao
import br.com.bluesoft.pronto.dao.UsuarioDao
import br.com.bluesoft.pronto.model.Categoria
import br.com.bluesoft.pronto.model.Classificacao
import br.com.bluesoft.pronto.model.Sprint
import br.com.bluesoft.pronto.model.Ticket
import br.com.bluesoft.pronto.model.TicketOrdem
import br.com.bluesoft.pronto.service.Seguranca
import br.com.bluesoft.pronto.to.TicketFilter;

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap

@Controller
@RequestMapping("/backlogs")
class BacklogController {
	
	private static final String VIEW_LISTAR = "/ticket/ticket.listar.jsp"
	private static final String VIEW_LISTAR_AGRUPADO = "/ticket/ticket.listarAgrupado.jsp"
	private static final String VIEW_ESTIMAR = "/ticket/ticket.estimar.jsp"
	private static final String VIEW_PRIORIZAR = "/ticket/ticket.priorizar.jsp"
	
	@Autowired SessionFactory sessionFactory
	@Autowired ClienteDao clienteDao
	@Autowired TicketDao ticketDao
	@Autowired SprintDao sprintDao
	@Autowired UsuarioDao usuarioDao
	@Autowired KanbanStatusDao kanbanStatusDao
	@Autowired TipoDeTicketDao tipoDeTicketDao
	@Autowired BacklogDao backlogDao
	@Autowired ConfiguracaoDao configuracaoDao
	@Autowired CategoriaDao categoriaDao
	@Autowired ProjetoDao projetoDao
	
	@ModelAttribute("categorias")
	List<Categoria> getCategorias() {
		categoriaDao.listar()
	}
	
	@ModelAttribute("tiposDeTicket")
	List<TipoDeTicket> getTiposDeTicket() {
		tipoDeTicketDao.listarTiposParaConsulta()
	}
	
	@RequestMapping(value='/productBacklog', method=GET)
	String productBacklog(final Model model) {
		"forward:/app/backlogs/" + Backlog.PRODUCT_BACKLOG
	}

	@RequestMapping(value='/inbox', method=GET)
	String inbox(final Model model) {
		"forward:/app/backlogs/" + Backlog.INBOX
	}

	@RequestMapping(value='/futuro', method=GET)
	String futuro(final Model model) {
		"forward:/app/backlogs/" + Backlog.FUTURO
	}

	@RequestMapping(value='/impedimentos', method=GET)
	String impedimentos(final Model model) {
		"forward:/app/backlogs/" + Backlog.IMPEDIMENTOS
	}

	@RequestMapping(value='/lixeira', method=GET)
	String lixeira(final Model model) {
		"forward:/app/backlogs/" + Backlog.LIXEIRA
	}
	
	@RequestMapping(value='/{backlogKey}', method=GET)
	String listarPorBacklog( Model model, @PathVariable int backlogKey, Integer categoriaKey, Integer tipoDeTicketKey, Integer kanbanStatusKey) {
		
		Seguranca.validarPermissao Papel.PRODUCT_OWNER, Papel.EQUIPE, Papel.SCRUM_MASTER
		
		def tickets = ticketDao.listarEstoriasEDefeitosPorBacklog(backlogKey, categoriaKey, tipoDeTicketKey, kanbanStatusKey)
		def tarefasSoltas = ticketDao.listarTarefasEmBacklogsDiferentesDasEstoriasPorBacklog(backlogKey)
		
		model.addAttribute "tickets", tickets
		model.addAttribute "tarefasSoltas", tarefasSoltas
		model.addAttribute "backlog", backlogDao.obter(backlogKey)
		model.addAttribute "kanbanStatus", kanbanStatusDao.listar()
		model.addAttribute "projetos", projetoDao.listarProjetosComSprintsAtivos()
		
		model.addAttribute "valorDeNegocioTotal", getValorDeNegocioTotal(tickets)
		model.addAttribute "esforcoTotal", getEsforcoTotal(tickets)
		model.addAttribute "tempoDeVidaMedioEmDias", getTempoDeVidaMedioEmDias(tickets)
		
		def totaisPorTipoDeTicket = totaisPorTipoDeTicket(tickets, tarefasSoltas)
		model.addAttribute "descricaoTotal", montaDescricaoTotal(totaisPorTipoDeTicket)
		
		VIEW_LISTAR
	}
	
	@RequestMapping(value="/sprints/{sprintKey}", method=GET)
	String listarPorSprint( Model model,  @PathVariable int sprintKey, Integer categoriaKey, Integer tipoDeTicketKey, Integer kanbanStatusKey) {
		
		Seguranca.validarPermissao Papel.PRODUCT_OWNER, Papel.EQUIPE, Papel.SCRUM_MASTER
		
		def tickets = ticketDao.listarEstoriasEDefeitosPorSprint(sprintKey, categoriaKey, tipoDeTicketKey, kanbanStatusKey)
		model.addAttribute "tickets", tickets
		model.addAttribute "sprint", sprintDao.obter(sprintKey)
		model.addAttribute "projetos", projetoDao.listarProjetosComSprintsAtivos()
		model.addAttribute "kanbanStatus", kanbanStatusDao.listar()
		
		model.addAttribute "valorDeNegocioTotal", getValorDeNegocioTotal(tickets)
		model.addAttribute "esforcoTotal", getEsforcoTotal(tickets)
		model.addAttribute "tempoDeVidaMedioEmDias", getTempoDeVidaMedioEmDias(tickets)
		
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
			model.addAttribute "mensagem", "Por favor, informe qual é o sprint atual."
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
	String listarTicketsPendentesPorCliente( Model model, @PathVariable Integer clienteKey, Integer kanbanStatusKey, String ordem,  String classificacao) {
		
		Seguranca.validarPermissao Papel.PRODUCT_OWNER, Papel.EQUIPE, Papel.SCRUM_MASTER
		
		TicketOrdem ticketOrdem = TicketOrdem.PRIORIDADE_DO_CLIENTE
		
		if (ordem != null && ordem.length() > 0) {
			ticketOrdem = TicketOrdem.valueOf(ordem)
		}
		
		Classificacao ticketClassificacao = Classificacao.ASCENDENTE
		if (classificacao != null && classificacao.length() > 0) {
			ticketClassificacao = Classificacao.valueOf(classificacao)
		}
		
		TicketFilter filter = new TicketFilter()
		filter.kanbanStatusKey = kanbanStatusKey
		filter.clienteKey = clienteKey
		filter.ordem = ticketOrdem
		filter.classificacao = ticketClassificacao
		filter.ignorarLixeira = true
		def tickets = ticketDao.buscar(filter)
		
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
		List tickets = ticketDao.listarEstoriasEDefeitosPorBacklog(backlogKey)
		model.addAttribute("tickets", tickets)
		model.addAttribute("backlog", sessionFactory.getCurrentSession().get(Backlog.class, backlogKey))
		model.addAttribute "configuracoes", configuracaoDao.getMapa()
		return VIEW_ESTIMAR
	}
	
	@RequestMapping(value="/{backlogKey}/priorizar", method=GET)
	String priorizarBacklog( Model model, @PathVariable  int backlogKey) {
		
		Seguranca.validarPermissao(Papel.PRODUCT_OWNER)
		
		def mapa = [:]
		
		def tickets = ticketDao.listarEstoriasEDefeitosPorBacklog(backlogKey)
		tickets.each {
			if (mapa[it.valorDeNegocio] == null) {
				mapa[it.valorDeNegocio] = [] as List
			}
			mapa[it.valorDeNegocio].add(it)
		}
		
		model.addAttribute("mapa", mapa)
		model.addAttribute("valores", mapa.keySet())
		model.addAttribute("backlog", sessionFactory.getCurrentSession().get(Backlog.class, backlogKey))
		return VIEW_PRIORIZAR
		
	}
	
	@RequestMapping(value="/{backlogKey}/priorizar", method=POST)
	@ResponseBody String priorizarBacklog( Model model, @PathVariable int backlogKey, Integer[] ticketKey, Integer valor) {
		def tx = ticketDao.session.beginTransaction()
		ticketDao.priorizar(ticketKey, valor)
		tx.commit()
		"true"
	}
	
	String montaDescricaoTotal( Map<Integer, Integer> totaisPorTipoDeTicket) {
		Integer totalDeDefeitos = totaisPorTipoDeTicket.get(TipoDeTicket.DEFEITO)
		Integer totalDeEstorias = totaisPorTipoDeTicket.get(TipoDeTicket.ESTORIA)
		Integer totalDeTarefas = totaisPorTipoDeTicket.get(TipoDeTicket.TAREFA)
		
		StringBuilder descricaoTotal = new StringBuilder()
		descricaoTotal.append(totalDeDefeitos > 0 ? totalDeDefeitos + " defeito(s), " : "nenhum defeito, ")
		descricaoTotal.append(totalDeEstorias > 0 ? totalDeEstorias + " estória(s), " : "nenhuma estória, ")
		descricaoTotal.append(totalDeTarefas > 0 ? totalDeTarefas + " tarefa(s), " : "nenhuma tarefa")
		
		return descricaoTotal.toString()
	}
	
	Map<Integer, Integer> totaisPorTipoDeTicket( List... listas) {
		Map<Integer, Integer> totais = new HashMap<Integer, Integer>()
		totais.put(TipoDeTicket.DEFEITO, 0)
		totais.put(TipoDeTicket.ESTORIA, 0)
		totais.put(TipoDeTicket.TAREFA, 0)
		
		for ( List tickets : listas) {
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

	int getValorDeNegocioTotal( List tickets) {
		int total = 0
		if (tickets != null) {
			for (final Ticket ticket : tickets) {
				if (!ticket.isTarefa()) {
					total += ticket.getValorDeNegocio()
				}
			}
		}
		return total
	}

	double getEsforcoTotal( List tickets) {
		double total = 0
		if (tickets != null) {
			for (final Ticket ticket : tickets) {
				if (!ticket.isTarefa()) {
					total += ticket.getEsforco()
				}
			}
		}
		return total
	}

	Integer getTempoDeVidaMedioEmDias( List tickets) {
		int total = 0
		int quantidade = 0
		if (tickets != null) {
			for (final Ticket ticket : tickets) {
				if (!ticket.isTarefa()) {
					quantidade++
					total += ticket.getTempoDeVidaEmDias()
				}
			}
		}
		
		return (quantidade > 0) ? (total / quantidade) : quantidade
	}
}
