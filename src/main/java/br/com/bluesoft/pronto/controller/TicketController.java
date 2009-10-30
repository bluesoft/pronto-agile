/*
 * Copyright 2009 Pronto Agile Project Management.
 *
 * This file is part of Pronto.
 *
 * Pronto is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Pronto is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Pronto. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package br.com.bluesoft.pronto.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.ProntoException;
import br.com.bluesoft.pronto.SegurancaException;
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
import br.com.bluesoft.pronto.model.Anexo;
import br.com.bluesoft.pronto.model.Classificacao;
import br.com.bluesoft.pronto.model.Sprint;
import br.com.bluesoft.pronto.model.Ticket;
import br.com.bluesoft.pronto.model.TicketLog;
import br.com.bluesoft.pronto.model.TicketOrdem;
import br.com.bluesoft.pronto.model.Usuario;
import br.com.bluesoft.pronto.service.Config;
import br.com.bluesoft.pronto.service.Seguranca;
import br.com.bluesoft.pronto.util.ControllerUtil;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

@Controller
public class TicketController {

	private static final String VIEW_LISTAR = "ticket.listar.jsp";
	private static final String VIEW_BRANCHES = "ticket.branches.jsp";
	private static final String VIEW_ESTIMAR = "ticket.estimar.jsp";
	private static final String VIEW_EDITAR = "ticket.editar.jsp";
	private static final String VIEW_LISTAR_AGRUPADO = "ticket.listarAgrupado.jsp";

	@Autowired
	private ClienteDao clienteDao;

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private TicketDao ticketDao;

	@Autowired
	private SprintDao sprintDao;

	@Autowired
	private UsuarioDao usuarioDao;

	@Autowired
	private KanbanStatusDao kanbanStatusDao;

	@Autowired
	private TipoDeTicketDao tipoDeTicketDao;

	@Autowired
	private BacklogDao backlogDao;

	@ModelAttribute("usuarios")
	public List<Usuario> getUsuarios() {
		return usuarioDao.listar();
	}

	@ModelAttribute("tiposDeTicket")
	public List<TipoDeTicket> getTiposDeTicket() {
		return tipoDeTicketDao.listar();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/ticket/listarPorBacklog.action")
	public String listarPorBacklog(final Model model, final int backlogKey) throws SegurancaException {

		Seguranca.validarPermissao(Papel.PRODUCT_OWNER, Papel.EQUIPE, Papel.SCRUM_MASTER);

		final List<Ticket> tickets = ticketDao.listarEstoriasEDefeitosPorBacklog(backlogKey);
		final List<Ticket> tarefasSoltas = ticketDao.listarTarefasEmBacklogsDiferentesDasEstoriasPorBacklog(backlogKey);

		model.addAttribute("tickets", tickets);
		model.addAttribute("tarefasSoltas", tarefasSoltas);
		model.addAttribute("backlog", backlogDao.obter(backlogKey));

		final Map<Integer, Integer> totaisPorTipoDeTicket = totaisPorTipoDeTicket(tickets, tarefasSoltas);
		model.addAttribute("descricaoTotal", montaDescricaoTotal(totaisPorTipoDeTicket));

		return VIEW_LISTAR;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/ticket/listarPorSprint.action")
	public String listarPorSprint(final Model model, final int sprintKey) throws SegurancaException {

		Seguranca.validarPermissao(Papel.PRODUCT_OWNER, Papel.EQUIPE, Papel.SCRUM_MASTER);

		final List<Ticket> tickets = ticketDao.listarEstoriasEDefeitosPorSprint(sprintKey);
		model.addAttribute("tickets", tickets);
		model.addAttribute("sprint", sprintDao.obter(sprintKey));
		model.addAttribute("sprints", sprintDao.listarSprintsEmAberto());

		final Map<Integer, Integer> totaisPorTipoDeTicket = totaisPorTipoDeTicket(tickets);
		model.addAttribute("descricaoTotal", montaDescricaoTotal(totaisPorTipoDeTicket));

		return VIEW_LISTAR;
	}

	@RequestMapping("/ticket/listarPendentesPorCliente.action")
	public String listarTicketsPendentesPorCliente(final Model model, final Integer kanbanStatusKey, final Integer clienteKey, final String ordem, final String classificacao) throws SegurancaException {

		Seguranca.validarPermissao(Papel.PRODUCT_OWNER, Papel.EQUIPE, Papel.SCRUM_MASTER);

		TicketOrdem ticketOrdem = TicketOrdem.TITULO;

		if (ordem != null && ordem.length() > 0) {
			ticketOrdem = TicketOrdem.valueOf(ordem);
		}

		Classificacao ticketClassificacao = Classificacao.ASCENDENTE;
		if (classificacao != null && classificacao.length() > 0) {
			ticketClassificacao = Classificacao.valueOf(classificacao);
		}

		List<Ticket> tickets = null;
		tickets = ticketDao.buscar(null, kanbanStatusKey, clienteKey, ticketOrdem, ticketClassificacao);

		final Multimap<String, Ticket> ticketsAgrupados = ArrayListMultimap.create();
		for (final Ticket ticket : tickets) {
			// Não exibe nem as tarefas nem os itens na lixeira
			if (ticket.getPai() == null && ticket.getBacklog().getBacklogKey() != Backlog.LIXEIRA) {
				ticketsAgrupados.put(ticket.getCliente() != null ? ticket.getCliente().getNome() : "Indefinido", ticket);
			}
		}

		model.addAttribute("ticketsAgrupados", ticketsAgrupados.asMap());

		final ArrayList<String> grupo = new ArrayList<String>(ticketsAgrupados.keySet());
		Collections.sort(grupo);

		model.addAttribute("grupos", grupo);
		model.addAttribute("clienteKey", clienteKey);
		model.addAttribute("kanbanStatusKey", kanbanStatusKey);
		model.addAttribute("kanbanStatus", kanbanStatusDao.listar());
		model.addAttribute("clientes", clienteDao.listar());
		model.addAttribute("ordens", TicketOrdem.values());
		model.addAttribute("ordem", ticketOrdem);
		model.addAttribute("classificacoes", Classificacao.values());
		model.addAttribute("classificacao", ticketClassificacao);

		return VIEW_LISTAR_AGRUPADO;
	}

	private String montaDescricaoTotal(final Map<Integer, Integer> totaisPorTipoDeTicket) throws SegurancaException {

		final Integer totalDeDefeitos = totaisPorTipoDeTicket.get(TipoDeTicket.DEFEITO);
		final Integer totalDeEstorias = totaisPorTipoDeTicket.get(TipoDeTicket.ESTORIA);
		final Integer totalDeTarefas = totaisPorTipoDeTicket.get(TipoDeTicket.TAREFA);
		final Integer totalDeIdeias = totaisPorTipoDeTicket.get(TipoDeTicket.IDEIA);

		final StringBuilder descricaoTotal = new StringBuilder();
		descricaoTotal.append(totalDeDefeitos > 0 ? totalDeDefeitos + " defeito(s), " : "nenhum defeito, ");
		descricaoTotal.append(totalDeEstorias > 0 ? totalDeEstorias + " estória(s), " : "nenhuma estória, ");
		descricaoTotal.append(totalDeTarefas > 0 ? totalDeTarefas + " tarefa(s), " : "nenhuma tarefa, ");
		descricaoTotal.append(totalDeIdeias > 0 ? totalDeIdeias + " ideia(s)" : "nenhuma ideia");

		return descricaoTotal.toString();
	}

	private Map<Integer, Integer> totaisPorTipoDeTicket(final List<Ticket>... listas) {
		final Map<Integer, Integer> totais = new HashMap<Integer, Integer>();
		totais.put(TipoDeTicket.DEFEITO, 0);
		totais.put(TipoDeTicket.ESTORIA, 0);
		totais.put(TipoDeTicket.TAREFA, 0);
		totais.put(TipoDeTicket.IDEIA, 0);

		for (final List<Ticket> tickets : listas) {
			for (final Ticket ticket : tickets) {
				totais.put(ticket.getTipoDeTicket().getTipoDeTicketKey(), totais.get(ticket.getTipoDeTicket().getTipoDeTicketKey()) + 1);
				for (final Ticket tarefa : ticket.getFilhos()) {
					if (tarefa.getBacklog().getBacklogKey() == ticket.getBacklog().getBacklogKey()) {
						totais.put(tarefa.getTipoDeTicket().getTipoDeTicketKey(), totais.get(tarefa.getTipoDeTicket().getTipoDeTicketKey()) + 1);
					}
				}
			}
		}

		return totais;
	}

	@RequestMapping("/ticket/branches.action")
	public String branches(final Model model) {
		final List<Ticket> tickets = ticketDao.listarTicketsQueNaoEstaoNoBranchMaster();
		model.addAttribute("tickets", tickets);
		return VIEW_BRANCHES;
	}

	@RequestMapping("/ticket/sprintAtual.action")
	public String sprintAtual(final Model model) {
		final Sprint sprint = sprintDao.getSprintAtual();
		final int quantidadeDeSprints = sprintDao.listar().size();

		if (quantidadeDeSprints == 0) {
			model.addAttribute("mensagem", "Por favor, crie um sprint.");
			return "forward:/sprint/listar.action";
		} else if (sprint == null) {
			model.addAttribute("mensagem", "Por favor, informe qual é o Sprint atual.");
			return "forward:/sprint/listar.action";
		} else {
			return "forward:/ticket/listarPorSprint.action?sprintKey=" + sprint.getSprintKey();
		}
	}

	@RequestMapping("/ticket/salvar.action")
	public String salvar(final Model model, final Ticket ticket, final String comentario, final String[] desenvolvedor, final String[] testador, final Integer paiKey, final Integer clienteKey) throws SegurancaException {

		Seguranca.validarPermissao(Papel.PRODUCT_OWNER, Papel.EQUIPE, Papel.SCRUM_MASTER);

		try {
			final Transaction tx = sessionFactory.getCurrentSession().beginTransaction();

			if (ticket.getTitulo() == null || ticket.getTitulo().trim().length() <= 0) {
				throw new ProntoException("Não é possível salvar uma estória, defeito ou tarefa sem descrição!");
			}

			if (paiKey == null) {

				ticket.setBacklog(backlogDao.obter(ticket.getBacklog().getBacklogKey()));
				ticket.setTipoDeTicket(tipoDeTicketDao.obter(ticket.getTipoDeTicket().getTipoDeTicketKey()));

				if (ticket.getSprint() != null && ticket.getSprint().getSprintKey() <= 0) {
					ticket.setSprint(null);
				} else {
					ticket.setSprint((Sprint) sessionFactory.getCurrentSession().get(Sprint.class, ticket.getSprint().getSprintKey()));
				}

			} else {
				final Ticket pai = ticketDao.obter(paiKey);
				copiarDadosDoPai(pai, ticket);
			}

			ticket.setKanbanStatus(kanbanStatusDao.obter(ticket.getKanbanStatus().getKanbanStatusKey()));

			if (clienteKey != null) {
				ticket.setCliente(clienteDao.obter(clienteKey));
			}

			if (ticket.getTicketKey() == 0) {
				ticket.setReporter(usuarioDao.obter(ticket.getReporter().getUsername()));
			}

			if (comentario != null && comentario.trim().length() > 0) {
				ticket.addComentario(comentario, Seguranca.getUsuario());
			}

			definirDesenvolvedores(ticket, desenvolvedor);
			definirTestadores(ticket, testador);

			ticketDao.salvar(ticket);

			tx.commit();

			return "redirect:editar.action?ticketKey=" + ticket.getTicketKey();
		} catch (final Exception e) {
			e.printStackTrace();
			model.addAttribute("erro", e.getMessage());
			return "forward:editar.action";
		}

	}

	private void definirDesenvolvedores(final Ticket ticket, final String[] desenvolvedor) throws SegurancaException {

		final Set<Usuario> desenvolvedoresAntigos = new TreeSet<Usuario>(ticketDao.listarDesenvolvedoresDoTicket(ticket.getTicketKey()));

		if (desenvolvedor != null && desenvolvedor.length > 0) {
			ticket.setDesenvolvedores(new TreeSet<Usuario>());
			for (final String username : desenvolvedor) {
				ticket.addDesenvolvedor((Usuario) sessionFactory.getCurrentSession().get(Usuario.class, username));
			}
		}

		final String desenvolvedoresAntigosStr = desenvolvedoresAntigos == null || desenvolvedoresAntigos.size() == 0 ? "nenhum" : desenvolvedoresAntigos.toString();
		final String desenvolvedoresNovosStr = ticket.getDesenvolvedores() == null || ticket.getDesenvolvedores().size() == 0 ? "nenhum" : ticket.getDesenvolvedores().toString();
		if (!desenvolvedoresAntigosStr.equals(desenvolvedoresNovosStr)) {
			ticket.addLogDeAlteracao("desenvolvedores", desenvolvedoresAntigosStr, desenvolvedoresNovosStr);
		}
	}

	private void definirTestadores(final Ticket ticket, final String[] testador) throws SegurancaException {

		final Set<Usuario> testadoresAntigos = new TreeSet<Usuario>(ticketDao.listarTestadoresDoTicket(ticket.getTicketKey()));

		if (testador != null && testador.length > 0) {
			ticket.setTestadores(new TreeSet<Usuario>());
			for (final String username : testador) {
				ticket.addTestador(usuarioDao.obter(username));
			}
		}

		final String testadoresAntigosStr = testadoresAntigos == null || testadoresAntigos.size() == 0 ? "nenhum" : testadoresAntigos.toString();
		final String testadoresNovosStr = ticket.getTestadores() == null || ticket.getTestadores().size() == 0 ? "nenhum" : ticket.getTestadores().toString();
		if (!testadoresAntigosStr.equals(testadoresNovosStr)) {
			ticket.addLogDeAlteracao("testadores", testadoresAntigosStr, testadoresNovosStr);
		}
	}

	@RequestMapping("/ticket/jogarNoLixo.action")
	public String jogarNoLixo(final Model model, final int ticketKey, final HttpServletResponse response) throws SegurancaException {

		Seguranca.validarPermissao(Papel.PRODUCT_OWNER, Papel.EQUIPE);

		final Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey);
		ticket.setBacklog((Backlog) sessionFactory.getCurrentSession().get(Backlog.class, Backlog.LIXEIRA));
		ticketDao.salvar(ticket);
		return "redirect:/ticket/editar.action?ticketKey=" + ticketKey;
	}

	@RequestMapping("/ticket/moverParaImpedimentos.action")
	public String moverParaImpedimentos(final Model model, final int ticketKey, final HttpServletResponse response) throws SegurancaException {

		Seguranca.validarPermissao(Papel.PRODUCT_OWNER, Papel.EQUIPE);

		final Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey);
		ticket.setBacklog((Backlog) sessionFactory.getCurrentSession().get(Backlog.class, Backlog.IMPEDIMENTOS));
		ticketDao.salvar(ticket);
		return "redirect:/ticket/editar.action?ticketKey=" + ticketKey;
	}

	@RequestMapping("/ticket/moverParaBranchMaster.action")
	public String moverParaBranchMaster(final Model model, final int ticketKey, final HttpServletResponse response) throws SegurancaException {

		Seguranca.validarPermissao(Papel.PRODUCT_OWNER, Papel.EQUIPE);

		final Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey);
		ticket.setBranch(Ticket.BRANCH_MASTER);
		ticketDao.salvar(ticket);
		return null;
	}

	@RequestMapping("/ticket/moverParaProductBacklog.action")
	public String moverParaProductBacklog(final Model model, final int ticketKey, final HttpServletResponse response) throws SegurancaException {

		final Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey);

		final int backlogDeOrigem = ticket.getBacklog().getBacklogKey();
		if (backlogDeOrigem == Backlog.IDEIAS) {
			Seguranca.validarPermissao(Papel.PRODUCT_OWNER);
		}

		ticket.setBacklog((Backlog) sessionFactory.getCurrentSession().get(Backlog.class, Backlog.PRODUCT_BACKLOG));

		// caso não seja uma ideia mantém o tipo original
		if (ticket.getTipoDeTicket().getTipoDeTicketKey() == TipoDeTicket.IDEIA) {
			ticket.setTipoDeTicket((TipoDeTicket) sessionFactory.getCurrentSession().get(TipoDeTicket.class, TipoDeTicket.ESTORIA));
		}

		ticket.setSprint(null);
		ticketDao.salvar(ticket);
		return "redirect:/ticket/editar.action?ticketKey=" + ticketKey;
	}

	@RequestMapping("/ticket/moverParaOSprintAtual.action")
	public String moverParaOSprintAtual(final Model model, final int ticketKey, final HttpServletResponse response) throws ProntoException {

		Seguranca.validarPermissao(Papel.PRODUCT_OWNER);

		final Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey);

		final int backlogDeOrigem = ticket.getBacklog().getBacklogKey();
		if (backlogDeOrigem != Backlog.PRODUCT_BACKLOG) {
			throw new ProntoException("Para que uma Estória ou Bug seja movida para o Sprint Atual é preciso que ela esteja no Product Backlog.");
		}

		ticket.setSprint(sprintDao.getSprintAtual());
		ticket.setBacklog(backlogDao.obter(Backlog.SPRINT_BACKLOG));
		ticketDao.salvar(ticket);
		return "redirect:/ticket/editar.action?ticketKey=" + ticketKey;

	}

	@RequestMapping("/ticket/moverParaIdeias.action")
	public String moverParaIdeias(final Model model, final int ticketKey, final HttpServletResponse response) throws SegurancaException {

		Seguranca.validarPermissao(Papel.PRODUCT_OWNER);

		final Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey);
		ticket.setBacklog((Backlog) sessionFactory.getCurrentSession().get(Backlog.class, Backlog.IDEIAS));
		ticket.setTipoDeTicket((TipoDeTicket) sessionFactory.getCurrentSession().get(TipoDeTicket.class, TipoDeTicket.IDEIA));
		ticket.setSprint(null);
		ticketDao.salvar(ticket);
		return "redirect:/ticket/editar.action?ticketKey=" + ticketKey;
	}

	@RequestMapping("/ticket/restaurar.action")
	public String restaurar(final Model model, final int ticketKey, final HttpServletResponse response) throws SegurancaException {

		Seguranca.validarPermissao(Papel.PRODUCT_OWNER, Papel.EQUIPE);

		final Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey);

		Backlog backlog = null;
		switch (ticket.getTipoDeTicket().getTipoDeTicketKey()) {
			case TipoDeTicket.ESTORIA:
			case TipoDeTicket.DEFEITO:
				backlog = (Backlog) sessionFactory.getCurrentSession().get(Backlog.class, Backlog.PRODUCT_BACKLOG);
				break;
			case TipoDeTicket.IDEIA:
				backlog = (Backlog) sessionFactory.getCurrentSession().get(Backlog.class, Backlog.IDEIAS);
				break;
			case TipoDeTicket.TAREFA:
				backlog = ticket.getPai().getBacklog();
				break;
		}

		ticket.setBacklog(backlog);

		if (!ticket.isTarefa() && ticket.getSprint() != null && ticket.getSprint().isFechado()) {
			ticket.setSprint(null);
		}

		ticketDao.salvar(ticket);

		if (ticket.getFilhos() != null) {
			for (final Ticket filho : ticket.getFilhos()) {
				filho.setBacklog(backlog);
				ticketDao.salvar(ticket);
			}
		}

		return "redirect:/ticket/editar.action?ticketKey=" + ticketKey;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/ticket/upload.action")
	public String upload(final HttpServletRequest request, final int ticketKey) throws Exception {

		final FileItemFactory factory = new DiskFileItemFactory();
		final ServletFileUpload upload = new ServletFileUpload(factory);

		final List<FileItem> items = upload.parseRequest(request);
		final String ticketDir = Config.getImagesFolder() + ticketKey + "/";
		final File dir = new File(ticketDir);
		dir.mkdirs();

		for (final FileItem fileItem : items) {
			final String nomeDoArquivo = fileItem.getName().toLowerCase().replace(' ', '_').replaceAll("[^A-Za-z0-9._\\-]", "");
			fileItem.write(new File(ticketDir + nomeDoArquivo));
		}

		return "redirect:editar.action?ticketKey=" + ticketKey;
	}

	private List<Anexo> listarAnexos(final int ticketKey) {
		final File folder = new File(Config.getImagesFolder() + ticketKey);
		if (folder.exists()) {
			final List<Anexo> anexos = new ArrayList<Anexo>();
			final String[] files = folder.list();
			Arrays.sort(files);
			for (final String file : files) {
				anexos.add(new Anexo(file));
			}
			return anexos;
		} else {
			return null;
		}

	}

	@RequestMapping("/ticket/excluirAnexo.action")
	public String excluirAnexo(final String file, final int ticketKey) throws Exception {
		final File arquivo = new File(Config.getImagesFolder() + ticketKey + "/" + file);
		if (arquivo.exists()) {
			arquivo.delete();
		}
		return "redirect:editar.action?ticketKey=" + ticketKey;
	}

	@RequestMapping("/ticket/download.action")
	public String download(final HttpServletResponse response, final String file, final int ticketKey) throws Exception {

		final File arquivo = new File(Config.getImagesFolder() + ticketKey + "/" + file);
		final FileInputStream fis = new FileInputStream(arquivo);
		final int numberBytes = fis.available();
		final byte bytes[] = new byte[numberBytes];
		fis.read(bytes);
		fis.close();

		String extensao = null;
		if (file.lastIndexOf('.') > 0) {
			extensao = file.substring(file.lastIndexOf('.') + 1, file.length());
		}

		String mime = null;
		if (extensao == null) {
			mime = "text/plain";
		} else if (extensao.equalsIgnoreCase("png")) {
			mime = "images/png";
		} else if (extensao.equalsIgnoreCase("jpg") || extensao.equalsIgnoreCase("jpeg")) {
			mime = "images/jpeg";
		} else if (extensao.equalsIgnoreCase("gif")) {
			mime = "images/gif";
		} else if (extensao.equalsIgnoreCase("pdf")) {
			mime = "application/pdf";
		} else if (extensao.equalsIgnoreCase("xls") || extensao.equalsIgnoreCase("xlsx")) {
			mime = "application/vnd.ms-excel";
		} else if (extensao.equalsIgnoreCase("csv")) {
			mime = "text/csv";
		} else if (extensao.equalsIgnoreCase("txt")) {
			mime = "text/plain";
		} else if (extensao.equalsIgnoreCase("doc") || extensao.equalsIgnoreCase("docx")) {
			mime = "application/ms-word";
		}

		response.addHeader("content-disposition", "attachment; filename=" + file);
		response.setContentType(mime);
		response.setContentLength(bytes.length);
		FileCopyUtils.copy(bytes, response.getOutputStream());

		return null;
	}

	@RequestMapping("/ticket/estimarSprint.action")
	public String estimarSprint(final Model model, final int sprintKey) throws SegurancaException {
		Seguranca.validarPermissao(Papel.EQUIPE, Papel.PRODUCT_OWNER);
		final List<Ticket> tickets = ticketDao.listarEstoriasEDefeitosPorSprint(sprintKey);
		model.addAttribute("tickets", tickets);
		model.addAttribute("sprint", sessionFactory.getCurrentSession().get(Sprint.class, sprintKey));
		return VIEW_ESTIMAR;
	}

	@RequestMapping("/ticket/estimarBacklog.action")
	public String estimarBacklog(final Model model, final int backlogKey) throws SegurancaException {
		Seguranca.validarPermissao(Papel.EQUIPE, Papel.PRODUCT_OWNER);
		final List<Ticket> tickets = ticketDao.listarEstoriasEDefeitosPorBacklog(backlogKey);
		model.addAttribute("tickets", tickets);
		model.addAttribute("backlog", sessionFactory.getCurrentSession().get(Backlog.class, backlogKey));
		return VIEW_ESTIMAR;
	}

	@RequestMapping("/ticket/salvarValorDeNegocio.action")
	public void salvarValorDeNegocio(final HttpServletResponse response, final int ticketKey, final int valorDeNegocio) throws SegurancaException {
		try {
			Seguranca.validarPermissao(Papel.PRODUCT_OWNER);
			final Ticket ticket = ticketDao.obter(ticketKey);
			ticket.setValorDeNegocio(valorDeNegocio);
			ticketDao.salvar(ticket);
			ControllerUtil.writeText(response, true);
		} catch (final Exception e) {
			ControllerUtil.writeText(response, false);
		}
	}

	@RequestMapping("/ticket/salvarBranch.action")
	public void salvarBranch(final HttpServletResponse response, final int ticketKey, final String branch) throws SegurancaException {
		try {
			Seguranca.validarPermissao(Papel.EQUIPE);
			final Ticket ticket = ticketDao.obter(ticketKey);
			ticket.setBranch(branch);
			ticketDao.salvar(ticket);
			ControllerUtil.writeText(response, true);
		} catch (final Exception e) {
			ControllerUtil.writeText(response, false);
		}
	}

	@RequestMapping("/ticket/salvarEsforco.action")
	public void salvarEsforco(final HttpServletResponse response, final int ticketKey, final double esforco) throws SegurancaException {
		try {
			Seguranca.validarPermissao(Papel.EQUIPE);
			final Ticket ticket = ticketDao.obter(ticketKey);
			ticket.setEsforco(esforco);
			ticketDao.salvar(ticket);
			ControllerUtil.writeText(response, true);
		} catch (final Exception e) {
			ControllerUtil.writeText(response, false);
		}
	}

	@RequestMapping("/ticket/salvarPar.action")
	public void salvarPar(final HttpServletResponse response, final int ticketKey, final boolean par) throws SegurancaException {
		try {
			Seguranca.validarPermissao(Papel.EQUIPE);
			final Ticket ticket = ticketDao.obter(ticketKey);
			ticket.setPar(par);
			ticketDao.salvar(ticket);
			ControllerUtil.writeText(response, true);
		} catch (final Exception e) {
			ControllerUtil.writeText(response, false);
		}
	}

	@RequestMapping("/ticket/listarTarefasParaAdicionarAoSprint.action")
	public String listarTarefasParaAdicionarAoSprint(final Model model, final int sprintKey) throws SegurancaException {

		Seguranca.validarPermissao(Papel.PRODUCT_OWNER, Papel.EQUIPE);

		model.addAttribute("sprint", sessionFactory.getCurrentSession().get(Sprint.class, sprintKey));
		model.addAttribute("tickets", ticketDao.listarEstoriasEDefeitosDoProductBacklog());
		return "/ticket/ticket.adicionarAoSprint.jsp";
	}

	@RequestMapping("/ticket/adicionarAoSprint.action")
	public String adicionarAoSprint(final Model model, final int sprintKey, final int[] ticketKey) {

		final Sprint sprint = (Sprint) sessionFactory.getCurrentSession().get(Sprint.class, sprintKey);
		for (final int key : ticketKey) {
			final Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, key);
			ticket.setBacklog((Backlog) sessionFactory.getCurrentSession().get(Backlog.class, Backlog.SPRINT_BACKLOG));
			ticket.setSprint(sprint);
			ticketDao.salvar(ticket);
		}
		return "redirect:/ticket/listarPorSprint.action?sprintKey=" + sprintKey;
	}

	@RequestMapping("/ticket/logDescricao.action")
	public String logDescricao(final Model model, final int ticketHistoryKey) {

		final TicketLog log = (TicketLog) sessionFactory.getCurrentSession().get(TicketLog.class, ticketHistoryKey);
		model.addAttribute("log", log);

		return "/ticket/ticket.logDescricao.jsp";

	}

	@RequestMapping("/ticket/transformarEmEstoria.action")
	public String transformarEmEstoria(final Model model, final int ticketKey) throws SegurancaException {

		Seguranca.validarPermissao(Papel.PRODUCT_OWNER, Papel.EQUIPE);

		final Ticket ticket = ticketDao.obter(ticketKey);
		ticket.setTipoDeTicket((TipoDeTicket) sessionFactory.getCurrentSession().createCriteria(TipoDeTicket.class).add(Restrictions.eq("tipoDeTicketKey", TipoDeTicket.ESTORIA)).uniqueResult());
		ticketDao.salvar(ticket);
		return "forward:/ticket/editar.action";
	}

	@RequestMapping("/ticket/transformarEmDefeito.action")
	public String transformarEmDefeito(final Model model, final int ticketKey) throws SegurancaException {

		Seguranca.validarPermissao(Papel.PRODUCT_OWNER, Papel.EQUIPE);

		final Ticket ticket = ticketDao.obter(ticketKey);

		if (ticket.getFilhos() != null && ticket.getFilhos().size() > 0) {
			model.addAttribute("erro", "Essa estória possui tarefas e por isso não pode ser transformada em um defeito.");
		} else {
			ticket.setTipoDeTicket((TipoDeTicket) sessionFactory.getCurrentSession().createCriteria(TipoDeTicket.class).add(Restrictions.eq("tipoDeTicketKey", TipoDeTicket.DEFEITO)).uniqueResult());
			ticketDao.salvar(ticket);
		}
		return "forward:/ticket/editar.action";
	}

	@RequestMapping("/ticket/verDescricao.action")
	public String verDescricao(final Model model, final int ticketKey) throws Exception {
		final Ticket ticket = ticketDao.obter(ticketKey);
		model.addAttribute("ticket", ticket);
		model.addAttribute("anexos", listarAnexos(ticketKey));
		return "/ticket/ticket.preview.jsp";
	}

	@RequestMapping("/ticket/editar.action")
	public String editar(final Model model, final Integer ticketKey, final Integer tipoDeTicketKey, final Integer backlogKey) throws SegurancaException {

		Seguranca.validarPermissao(Papel.PRODUCT_OWNER, Papel.EQUIPE);

		if (ticketKey != null) {
			final Ticket ticket = ticketDao.obterComDependecias(ticketKey);

			if (ticket == null) {
				model.addAttribute("mensagem", "O Ticket #" + ticketKey + " não existe.");
				return "/branca.jsp";
			}
			model.addAttribute("sprints", sprintDao.listarSprintsEmAberto());
			model.addAttribute("ticket", ticket);
			model.addAttribute("anexos", listarAnexos(ticketKey));
		} else {
			final Ticket novoTicket = new Ticket();
			novoTicket.setReporter(Seguranca.getUsuario());
			novoTicket.setTipoDeTicket((TipoDeTicket) sessionFactory.getCurrentSession().get(TipoDeTicket.class, tipoDeTicketKey));
			novoTicket.setBacklog((Backlog) sessionFactory.getCurrentSession().get(Backlog.class, backlogKey));
			model.addAttribute("ticket", novoTicket);
			model.addAttribute("tipoDeTicketKey", tipoDeTicketKey);
		}

		model.addAttribute("clientes", clienteDao.listar());
		model.addAttribute("testadores", usuarioDao.listarEquipe());
		model.addAttribute("desenvolvedores", usuarioDao.listarEquipe());
		model.addAttribute("kanbanStatus", kanbanStatusDao.listar());
		return VIEW_EDITAR;
	}

	@RequestMapping("/ticket/incluirTarefa.action")
	public String incluirTarefa(final Model model, final int paiKey) throws Exception {

		Seguranca.validarPermissao(Papel.PRODUCT_OWNER, Papel.EQUIPE);

		final Ticket pai = ticketDao.obter(paiKey);

		final Ticket tarefa = new Ticket();
		copiarDadosDoPai(pai, tarefa);
		tarefa.setPrioridade(9999);

		model.addAttribute("ticket", tarefa);
		model.addAttribute("tipoDeTicketKey", TipoDeTicket.TAREFA);
		model.addAttribute("kanbanStatus", kanbanStatusDao.listar());
		model.addAttribute("testadores", usuarioDao.listarEquipe());
		model.addAttribute("desenvolvedores", usuarioDao.listarEquipe());

		return VIEW_EDITAR;
	}

	private void copiarDadosDoPai(final Ticket pai, final Ticket filho) {
		filho.setPai(pai);
		if (filho.getReporter() == null || filho.getReporter().getUsername() == null) {
			filho.setReporter(Seguranca.getUsuario());
		}
		filho.setTipoDeTicket(tipoDeTicketDao.obter(TipoDeTicket.TAREFA));
		filho.setBacklog(pai.getBacklog());
		filho.setSprint(pai.getSprint());
		filho.setCliente(pai.getCliente());
		filho.setSolicitador(pai.getSolicitador());
	}

	@RequestMapping("/ticket/alterarOrdemDasTarefas.action")
	public void alterarOrdem(final int pai, final int ticketKey[]) {
		final Ticket ticketPai = ticketDao.obterComDependecias(pai);
		int prioridade = 0;
		for (final int filhoKey : ticketKey) {
			final Ticket ticketFilho = ticketPai.getFilhoPorKey(filhoKey);
			if (ticketFilho != null) {
				ticketFilho.setPrioridade(++prioridade);
				ticketDao.salvar(ticketFilho);
			}
		}
	}
}
