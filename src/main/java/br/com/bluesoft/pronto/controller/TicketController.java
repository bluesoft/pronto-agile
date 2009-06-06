package br.com.bluesoft.pronto.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.SegurancaException;
import br.com.bluesoft.pronto.core.Backlog;
import br.com.bluesoft.pronto.core.KanbanStatus;
import br.com.bluesoft.pronto.core.Papel;
import br.com.bluesoft.pronto.core.TipoDeTicket;
import br.com.bluesoft.pronto.dao.SprintDao;
import br.com.bluesoft.pronto.dao.TicketDao;
import br.com.bluesoft.pronto.model.Sprint;
import br.com.bluesoft.pronto.model.Ticket;
import br.com.bluesoft.pronto.model.TicketLog;
import br.com.bluesoft.pronto.model.Usuario;
import br.com.bluesoft.pronto.service.Config;
import br.com.bluesoft.pronto.service.Seguranca;

@Controller
public class TicketController {

	private static final String VIEW_LISTAR = "ticket.listar.jsp";
	private static final String VIEW_ESTIMAR = "ticket.estimar.jsp";
	private static final String VIEW_EDITAR = "ticket.editar.jsp";

	@Autowired
	private Config config;

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private TicketDao ticketDao;

	@Autowired
	private SprintDao sprintDao;

	@ModelAttribute("usuarios")
	@SuppressWarnings("unchecked")
	public List<Usuario> getUsuarios() {
		return sessionFactory.getCurrentSession().createCriteria(Usuario.class).list();
	}

	@ModelAttribute("tiposDeTicket")
	@SuppressWarnings("unchecked")
	public List<Usuario> getTiposDeTicket() {
		return sessionFactory.getCurrentSession().createCriteria(TipoDeTicket.class).list();
	}

	@RequestMapping("/ticket/listarPorBacklog.action")
	public String listarPorBacklog(final Model model, final int backlogKey) {
		final List<Ticket> tickets = ticketDao.listarPorBacklog(backlogKey);
		model.addAttribute("tickets", tickets);
		model.addAttribute("backlog", sessionFactory.getCurrentSession().get(Backlog.class, backlogKey));
		return VIEW_LISTAR;
	}

	@RequestMapping("/ticket/sprintAtual.action")
	public String sprintAtual(final Model model) {
		final Sprint sprint = sprintDao.getSprintAtual();

		if (sprint == null) {
			model.addAttribute("mensagem", "Por favor, informe qual é o Sprint atual.");
			return "forward:/sprint/listar.action";
		} else {
			return "forward:/ticket/listarPorSprint.action?sprintKey=" + sprint.getSprintKey();
		}
	}

	@RequestMapping("/ticket/listarPorSprint.action")
	public String listarPorSprint(final Model model, final int sprintKey) {
		final List<Ticket> tickets = ticketDao.listarPorSprint(sprintKey);
		model.addAttribute("tickets", tickets);
		model.addAttribute("sprint", sprintDao.obter(sprintKey));
		return VIEW_LISTAR;
	}

	@RequestMapping("/ticket/salvar.action")
	public String salvar(final Model model, final Ticket ticket, final String comentario, final String[] desenvolvedor) {

		try {
			final Transaction tx = sessionFactory.getCurrentSession().beginTransaction();

			ticket.setBacklog((Backlog) sessionFactory.getCurrentSession().get(Backlog.class, ticket.getBacklog().getBacklogKey()));
			ticket.setTipoDeTicket((TipoDeTicket) sessionFactory.getCurrentSession().get(TipoDeTicket.class, ticket.getTipoDeTicket().getTipoDeTicketKey()));
			ticket.setReporter((Usuario) sessionFactory.getCurrentSession().get(Usuario.class, ticket.getReporter().getUsername()));
			ticket.setKanbanStatus((KanbanStatus) sessionFactory.getCurrentSession().get(KanbanStatus.class, ticket.getKanbanStatus().getKanbanStatusKey()));

			if (ticket.getSprint() != null && ticket.getSprint().getSprintKey() <= 0) {
				ticket.setSprint(null);
			} else {
				ticket.setSprint((Sprint) sessionFactory.getCurrentSession().get(Sprint.class, ticket.getSprint().getSprintKey()));
			}

			if (comentario != null && comentario.trim().length() > 0) {
				ticket.addComentario(comentario, Seguranca.getUsuario().getNome());
			}

			definirDesenvolvedores(ticket, desenvolvedor);

			ticketDao.salvar(ticket);

			tx.commit();

			return "redirect:editar.action?ticketKey=" + ticket.getTicketKey();
		} catch (final Exception e) {
			model.addAttribute("erro", e.getMessage());
			return "forward:editar.action";
		}

	}

	private void definirDesenvolvedores(final Ticket ticket, final String[] desenvolvedor) {

		final Set<Usuario> desenvolvedoresAntigos = new TreeSet<Usuario>(ticketDao.listarDesenvolvedoresDoTicket(ticket.getTicketKey()));

		if (desenvolvedor != null && desenvolvedor.length > 0) {
			ticket.setDesenvolvedores(new TreeSet<Usuario>());
			for (final String username : desenvolvedor) {
				ticket.addDesenvolvedor((Usuario) sessionFactory.getCurrentSession().get(Usuario.class, username));
			}
		}

		final String desenvolvedoresAntigosStr = desenvolvedoresAntigos == null ? "nenhum" : desenvolvedoresAntigos.toString();
		final String desenvolvedoresNovosStr = ticket.getDesenvolvedores() == null ? "nenhum" : ticket.getDesenvolvedores().toString();
		if (!desenvolvedoresAntigosStr.equals(desenvolvedoresNovosStr)) {
			ticket.addLogDeAlteracao("desenvolvedores", desenvolvedoresAntigosStr, desenvolvedoresNovosStr);
		}
	}

	@RequestMapping("/ticket/editar.action")
	public String editar(final Model model, final Integer ticketKey, final Integer tipoDeTicketKey, final Integer backlogKey) {
		if (ticketKey != null) {
			final Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey);

			if (ticket == null) {
				model.addAttribute("mensagem", "O Ticket #" + ticketKey + " não existe.");
				return "/branca.jsp";
			}
			model.addAttribute("sprints", sessionFactory.getCurrentSession().createCriteria(Sprint.class).list());
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
		model.addAttribute("usuarios", sessionFactory.getCurrentSession().createCriteria(Usuario.class).list());
		model.addAttribute("kanbanStatus", sessionFactory.getCurrentSession().createCriteria(KanbanStatus.class).list());
		return VIEW_EDITAR;
	}

	@RequestMapping("/ticket/jogarNoLixo.action")
	public String jogarNoLixo(final Model model, final int ticketKey, final HttpServletResponse response) {
		final Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey);
		ticket.setBacklog((Backlog) sessionFactory.getCurrentSession().get(Backlog.class, Backlog.LIXEIRA));
		ticket.setKanbanStatus((KanbanStatus) sessionFactory.getCurrentSession().get(KanbanStatus.class, KanbanStatus.TO_DO));
		sessionFactory.getCurrentSession().update(ticket);
		sessionFactory.getCurrentSession().flush();
		return "redirect:/ticket/editar.action?ticketKey=" + ticketKey;
	}

	@RequestMapping("/ticket/moverParaImpedimentos.action")
	public String moverParaImpedimentos(final Model model, final int ticketKey, final HttpServletResponse response) {
		final Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey);
		ticket.setBacklog((Backlog) sessionFactory.getCurrentSession().get(Backlog.class, Backlog.IMPEDIMENTOS));
		sessionFactory.getCurrentSession().update(ticket);
		sessionFactory.getCurrentSession().flush();
		return "redirect:/ticket/editar.action?ticketKey=" + ticketKey;
	}

	@RequestMapping("/ticket/moverParaProductBacklog.action")
	public String moverParaProductBacklog(final Model model, final int ticketKey, final HttpServletResponse response) throws SegurancaException {

		final Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey);

		final int backlogDeOrigem = ticket.getBacklog().getBacklogKey();
		if (backlogDeOrigem == Backlog.IDEIAS) {
			Seguranca.validarPermissao(Papel.PRODUCT_OWNER);
		}

		ticket.setBacklog((Backlog) sessionFactory.getCurrentSession().get(Backlog.class, Backlog.PRODUCT_BACKLOG));
		ticket.setTipoDeTicket((TipoDeTicket) sessionFactory.getCurrentSession().get(TipoDeTicket.class, TipoDeTicket.ESTORIA));
		ticket.setSprint(null);
		ticket.setKanbanStatus((KanbanStatus) sessionFactory.getCurrentSession().get(KanbanStatus.class, KanbanStatus.TO_DO));
		sessionFactory.getCurrentSession().update(ticket);
		sessionFactory.getCurrentSession().flush();
		return "redirect:/ticket/editar.action?ticketKey=" + ticketKey;
	}

	@RequestMapping("/ticket/moverParaIdeias.action")
	public String moverParaIdeias(final Model model, final int ticketKey, final HttpServletResponse response) throws SegurancaException {

		Seguranca.validarPermissao(Papel.PRODUCT_OWNER);

		final Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey);
		ticket.setBacklog((Backlog) sessionFactory.getCurrentSession().get(Backlog.class, Backlog.IDEIAS));
		ticket.setTipoDeTicket((TipoDeTicket) sessionFactory.getCurrentSession().get(TipoDeTicket.class, TipoDeTicket.IDEIA));
		ticket.setKanbanStatus((KanbanStatus) sessionFactory.getCurrentSession().get(KanbanStatus.class, KanbanStatus.TO_DO));
		ticket.setSprint(null);
		sessionFactory.getCurrentSession().update(ticket);
		sessionFactory.getCurrentSession().flush();
		return "redirect:/ticket/editar.action?ticketKey=" + ticketKey;
	}

	@RequestMapping("/ticket/restaurar.action")
	public String restaurar(final Model model, final int ticketKey, final HttpServletResponse response) {
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
		sessionFactory.getCurrentSession().update(ticket);
		sessionFactory.getCurrentSession().flush();
		return "redirect:/ticket/editar.action?ticketKey=" + ticketKey;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/ticket/upload.action")
	public String upload(final HttpServletRequest request, final int ticketKey) throws Exception {

		final FileItemFactory factory = new DiskFileItemFactory();
		final ServletFileUpload upload = new ServletFileUpload(factory);

		final List<FileItem> items = upload.parseRequest(request);
		final String ticketDir = config.getImagesFolder() + ticketKey + "/";
		final File dir = new File(ticketDir);
		dir.mkdirs();

		for (final FileItem fileItem : items) {
			fileItem.write(new File(ticketDir + fileItem.getName()));
		}

		return "redirect:editar.action?ticketKey=" + ticketKey;
	}

	private List<String> listarAnexos(final int ticketKey) {
		final File file = new File(config.getImagesFolder() + ticketKey);
		if (file.exists()) {
			return Arrays.asList(file.list());
		} else {
			return null;
		}
	}

	@RequestMapping("/ticket/excluirAnexo.action")
	public String excluirAnexo(final String file, final int ticketKey) throws Exception {
		final File arquivo = new File(config.getImagesFolder() + ticketKey + "/" + file);
		if (arquivo.exists()) {
			arquivo.delete();
		}
		return "redirect:editar.action?ticketKey=" + ticketKey;
	}

	@RequestMapping("/ticket/download.action")
	public String download(final HttpServletResponse response, final String file, final int ticketKey) throws Exception {

		final File arquivo = new File(config.getImagesFolder() + ticketKey + "/" + file);
		final FileInputStream fis = new FileInputStream(arquivo);
		final int numberBytes = fis.available();
		final byte bytes[] = new byte[numberBytes];
		fis.read(bytes);
		fis.close();

		response.getOutputStream().write(bytes);
		response.setHeader("Content-disposition", "attachment;filename=" + file);

		return null;

	}

	@RequestMapping("/ticket/estimarSprint.action")
	public String estimarSprint(final Model model, final int sprintKey) {
		final List<Ticket> tickets = ticketDao.listarPorSprint(sprintKey);
		model.addAttribute("tickets", tickets);
		model.addAttribute("sprint", sessionFactory.getCurrentSession().get(Sprint.class, sprintKey));
		return VIEW_ESTIMAR;
	}

	@RequestMapping("/ticket/estimarBacklog.action")
	public String estimarBacklog(final Model model, final int backlogKey) throws SegurancaException {
		Seguranca.validarPermissao(Papel.DESENVOLVEDOR, Papel.PRODUCT_OWNER);
		final List<Ticket> tickets = ticketDao.listarPorBacklog(backlogKey);
		model.addAttribute("tickets", tickets);
		model.addAttribute("backlog", sessionFactory.getCurrentSession().get(Backlog.class, backlogKey));
		return VIEW_ESTIMAR;
	}

	@RequestMapping("/ticket/salvarEstimativa.action")
	public String salvarEstimativa(final Model model, final int ticketKey[], final int valorDeNegocio[], final int esforco[]) throws SegurancaException {

		Seguranca.validarPermissao(Papel.DESENVOLVEDOR, Papel.PRODUCT_OWNER);

		Ticket ticket = null;
		for (int i = 0; i < ticketKey.length; i++) {
			ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey[i]);
			if (Seguranca.getUsuario().temOPapel(Papel.DESENVOLVEDOR)) {
				ticket.setEsforco(esforco[i]);
			}
			if (Seguranca.getUsuario().temOPapel(Papel.PRODUCT_OWNER)) {
				ticket.setValorDeNegocio(valorDeNegocio[i]);
			}
			sessionFactory.getCurrentSession().update(ticket);
		}
		sessionFactory.getCurrentSession().flush();

		if (ticket.getBacklog().isSprintBacklog()) {
			return "/ticket/listarPorSprint.action?sprintKey=" + ticket.getSprint().getSprintKey();
		} else {
			return "/ticket/listarPorBacklog.action?backlogKey=" + ticket.getBacklog().getBacklogKey();
		}

	}

	@RequestMapping("/ticket/listarTarefasParaAdicionarAoSprint.action")
	public String listarTarefasParaAdicionarAoSprint(final Model model, final int sprintKey) {
		model.addAttribute("sprint", sessionFactory.getCurrentSession().get(Sprint.class, sprintKey));
		model.addAttribute("tickets", sessionFactory.getCurrentSession().createCriteria(Ticket.class).createAlias("backlog", "backlog").add(Restrictions.eq("backlog.backlogKey", Backlog.PRODUCT_BACKLOG)).addOrder(Order.desc("valorDeNegocio")).addOrder(Order.desc("esforco")).list());
		return "/ticket/ticket.adicionarAoSprint.jsp";
	}

	@RequestMapping("/ticket/adicionarAoSprint.action")
	public String adicionarAoSprint(final Model model, final int sprintKey, final int[] ticketKey) {

		final Sprint sprint = (Sprint) sessionFactory.getCurrentSession().get(Sprint.class, sprintKey);
		for (final int key : ticketKey) {
			final Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, key);
			ticket.setBacklog((Backlog) sessionFactory.getCurrentSession().get(Backlog.class, Backlog.SPRINT_BACKLOG));
			ticket.setSprint(sprint);
			sessionFactory.getCurrentSession().update(ticket);
		}
		sessionFactory.getCurrentSession().flush();
		return "redirect:/ticket/listarPorSprint.action?sprintKey=" + sprintKey;
	}

	@RequestMapping("/ticket/logDescricao.action")
	public String logDescricao(final Model model, final int ticketHistoryKey) {

		final TicketLog log = (TicketLog) sessionFactory.getCurrentSession().get(TicketLog.class, ticketHistoryKey);
		model.addAttribute("log", log);

		return "/ticket/ticket.logDescricao.jsp";

	}

}
