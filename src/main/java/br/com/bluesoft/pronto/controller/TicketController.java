package br.com.bluesoft.pronto.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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

import br.com.bluesoft.pronto.core.Backlog;
import br.com.bluesoft.pronto.core.KanbanStatus;
import br.com.bluesoft.pronto.core.TipoDeTicket;
import br.com.bluesoft.pronto.model.Sprint;
import br.com.bluesoft.pronto.model.Ticket;
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

	@SuppressWarnings("unchecked")
	@RequestMapping("/ticket/listarPorBacklog.action")
	public String listarPorBacklog(final Model model, int backlogKey) {
		final List<Ticket> tickets = sessionFactory.getCurrentSession().createCriteria(Ticket.class).add(Restrictions.eq("backlog.backlogKey", backlogKey)).addOrder(Order.desc("valorDeNegocio")).addOrder(Order.desc("esforco")).list();
		model.addAttribute("tickets", tickets);
		model.addAttribute("backlog", sessionFactory.getCurrentSession().get(Backlog.class, backlogKey));
		return VIEW_LISTAR;
	}

	@RequestMapping("/ticket/sprintAtual.action")
	public String sprintAtual(final Model model) {
		Sprint sprint = (Sprint) sessionFactory.getCurrentSession().createCriteria(Sprint.class).add(Restrictions.eq("atual", true)).uniqueResult();

		if (sprint == null) {
			model.addAttribute("mensagem", "Por favor, informe qual é o Sprint atual.");
			return "forward:/sprint/listar.action";
		} else {
			return "forward:/ticket/listarPorSprint.action?sprintKey=" + sprint.getSprintKey();
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/ticket/listarPorSprint.action")
	public String listarPorSprint(final Model model, int sprintKey) {
		final List<Ticket> tickets = sessionFactory.getCurrentSession().createCriteria(Ticket.class).add(Restrictions.eq("sprint.sprintKey", sprintKey)).addOrder(Order.desc("valorDeNegocio")).addOrder(Order.desc("esforco")).list();
		model.addAttribute("tickets", tickets);
		model.addAttribute("sprint", sessionFactory.getCurrentSession().get(Sprint.class, sprintKey));
		return VIEW_LISTAR;
	}

	@RequestMapping("/ticket/salvar.action")
	public String salvar(final Model model, final Ticket ticket, String comentario, String[] desenvolvedor) {
		final Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		ticket.setBacklog((Backlog) sessionFactory.getCurrentSession().get(Backlog.class, ticket.getBacklog().getBacklogKey()));
		ticket.setTipoDeTicket((TipoDeTicket) sessionFactory.getCurrentSession().get(TipoDeTicket.class, ticket.getTipoDeTicket().getTipoDeTicketKey()));
		ticket.setReporter((Usuario) sessionFactory.getCurrentSession().get(Usuario.class, ticket.getReporter().getUsername()));

		if (ticket.getSprint() != null && ticket.getSprint().getSprintKey() <= 0) {
			ticket.setSprint(null);
		} else {
			ticket.setSprint((Sprint) sessionFactory.getCurrentSession().get(Sprint.class, ticket.getSprint().getSprintKey()));
		}

		if (comentario != null && comentario.trim().length() > 0) {
			ticket.addComentario(comentario, "andrefaria");
		}

		if (desenvolvedor != null && desenvolvedor.length > 0) {
			ticket.setDesenvolvedores(new HashSet<Usuario>());
			for (String username : desenvolvedor) {
				ticket.addDesenvolvedor((Usuario) sessionFactory.getCurrentSession().get(Usuario.class, username));
			}
		}

		sessionFactory.getCurrentSession().saveOrUpdate(ticket);
		sessionFactory.getCurrentSession().flush();

		tx.commit();
		return "redirect:editar.action?ticketKey=" + ticket.getTicketKey();
	}

	@RequestMapping("/ticket/editar.action")
	public String editar(final Model model, final Integer ticketKey, final Integer tipoDeTicketKey, final Integer backlogKey) {
		if (ticketKey != null) {
			final Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey);
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
	public String jogarNoLixo(Model model, int ticketKey, HttpServletResponse response) {
		Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey);
		ticket.setBacklog((Backlog) sessionFactory.getCurrentSession().get(Backlog.class, Backlog.LIXEIRA));
		sessionFactory.getCurrentSession().update(ticket);
		sessionFactory.getCurrentSession().flush();
		return "redirect:/ticket/editar.action?ticketKey=" + ticketKey;
	}

	@RequestMapping("/ticket/moverParaImpedimentos.action")
	public String moverParaImpedimentos(Model model, int ticketKey, HttpServletResponse response) {
		Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey);
		ticket.setBacklog((Backlog) sessionFactory.getCurrentSession().get(Backlog.class, Backlog.IMPEDIMENTOS));
		ticket.setTipoDeTicket((TipoDeTicket) sessionFactory.getCurrentSession().get(TipoDeTicket.class, TipoDeTicket.IMPEDIMENTO));
		sessionFactory.getCurrentSession().update(ticket);
		sessionFactory.getCurrentSession().flush();
		return "redirect:/ticket/editar.action?ticketKey=" + ticketKey;
	}

	@RequestMapping("/ticket/moverParaProductBacklog.action")
	public String moverParaProductBacklog(Model model, int ticketKey, HttpServletResponse response) {
		Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey);
		ticket.setBacklog((Backlog) sessionFactory.getCurrentSession().get(Backlog.class, Backlog.PRODUCT_BACKLOG));
		ticket.setTipoDeTicket((TipoDeTicket) sessionFactory.getCurrentSession().get(TipoDeTicket.class, TipoDeTicket.ESTORIA));
		sessionFactory.getCurrentSession().update(ticket);
		sessionFactory.getCurrentSession().flush();
		return "redirect:/ticket/editar.action?ticketKey=" + ticketKey;
	}

	@RequestMapping("/ticket/moverParaIdeias.action")
	public String moverParaIdeias(Model model, int ticketKey, HttpServletResponse response) {
		Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey);
		ticket.setBacklog((Backlog) sessionFactory.getCurrentSession().get(Backlog.class, Backlog.IDEIAS));
		ticket.setTipoDeTicket((TipoDeTicket) sessionFactory.getCurrentSession().get(TipoDeTicket.class, TipoDeTicket.IDEIA));
		sessionFactory.getCurrentSession().update(ticket);
		sessionFactory.getCurrentSession().flush();
		return "redirect:/ticket/editar.action?ticketKey=" + ticketKey;
	}

	@RequestMapping("/ticket/restaurar.action")
	public String restaurar(Model model, int ticketKey, HttpServletResponse response) {
		Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey);

		Backlog backlog = null;
		switch (ticket.getTipoDeTicket().getTipoDeTicketKey()) {
		case TipoDeTicket.ESTORIA:
		case TipoDeTicket.DEFEITO:
			backlog = (Backlog) sessionFactory.getCurrentSession().get(Backlog.class, Backlog.PRODUCT_BACKLOG);
			break;
		case TipoDeTicket.IDEIA:
			backlog = (Backlog) sessionFactory.getCurrentSession().get(Backlog.class, Backlog.IDEIAS);
			break;
		case TipoDeTicket.IMPEDIMENTO:
			backlog = (Backlog) sessionFactory.getCurrentSession().get(Backlog.class, Backlog.IMPEDIMENTOS);
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
	public String upload(HttpServletRequest request, int ticketKey) throws Exception {

		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);

		List<FileItem> items = upload.parseRequest(request);
		String ticketDir = config.getImagesFolder() + ticketKey + "/";
		File dir = new File(ticketDir);
		dir.mkdirs();

		for (FileItem fileItem : items) {
			fileItem.write(new File(ticketDir + fileItem.getName()));
		}

		return "redirect:editar.action?ticketKey=" + ticketKey;
	}

	private List<String> listarAnexos(int ticketKey) {
		File file = new File(config.getImagesFolder() + ticketKey);
		if (file.exists()) {
			return (Arrays.asList(file.list()));
		} else {
			return null;
		}
	}

	@RequestMapping("/ticket/excluirAnexo.action")
	public String excluirAnexo(String file, int ticketKey) throws Exception {
		File arquivo = new File(config.getImagesFolder() + ticketKey + "/" + file);
		if (arquivo.exists()) {
			arquivo.delete();
		}
		return "redirect:editar.action?ticketKey=" + ticketKey;
	}

	@RequestMapping("/ticket/download.action")
	public String download(HttpServletResponse response, String file, int ticketKey) throws Exception {

		File arquivo = new File(config.getImagesFolder() + ticketKey + "/" + file);
		FileInputStream fis = new FileInputStream(arquivo);
		int numberBytes = fis.available();
		byte bytes[] = new byte[numberBytes];
		fis.read(bytes);
		fis.close();

		response.getOutputStream().write(bytes);
		response.setHeader("Content-disposition", "attachment;filename=" + file);

		return null;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/ticket/estimarSprint.action")
	public String estimarSprint(final Model model, int sprintKey) {
		final List<Ticket> tickets = sessionFactory.getCurrentSession().createCriteria(Ticket.class).add(Restrictions.eq("sprint.sprintKey", sprintKey)).addOrder(Order.desc("valorDeNegocio")).addOrder(Order.desc("esforco")).list();
		model.addAttribute("tickets", tickets);
		model.addAttribute("sprint", sessionFactory.getCurrentSession().get(Sprint.class, sprintKey));
		return VIEW_ESTIMAR;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/ticket/estimarBacklog.action")
	public String estimarBacklog(final Model model, int backlogKey) {
		final List<Ticket> tickets = sessionFactory.getCurrentSession().createCriteria(Ticket.class).add(Restrictions.eq("backlog.backlogKey", backlogKey)).addOrder(Order.desc("valorDeNegocio")).addOrder(Order.desc("esforco")).list();
		model.addAttribute("tickets", tickets);
		model.addAttribute("backlog", sessionFactory.getCurrentSession().get(Backlog.class, backlogKey));
		return VIEW_ESTIMAR;
	}

	@RequestMapping("/ticket/salvarEstimativa.action")
	public String salvarEstimativa(final Model model, int ticketKey[], int valorDeNegocio[], int esforco[]) {

		Ticket ticket = null;
		for (int i = 0; i < ticketKey.length; i++) {
			ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey[i]);
			ticket.setEsforco(esforco[i]);
			ticket.setValorDeNegocio(valorDeNegocio[i]);
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
	public String listarTarefasParaAdicionarAoSprint(final Model model, int sprintKey) {
		model.addAttribute("sprint", sessionFactory.getCurrentSession().get(Sprint.class, sprintKey));
		model.addAttribute("tickets", sessionFactory.getCurrentSession().createCriteria(Ticket.class).createAlias("backlog", "backlog").add(Restrictions.eq("backlog.backlogKey", Backlog.PRODUCT_BACKLOG)).addOrder(Order.desc("valorDeNegocio")).addOrder(Order.desc("esforco")).list());
		return "/ticket/ticket.adicionarAoSprint.jsp";
	}

	@RequestMapping("/ticket/adicionarAoSprint.action")
	public String adicionarAoSprint(final Model model, int sprintKey, int[] ticketKey) {
		
		Sprint sprint = (Sprint) sessionFactory.getCurrentSession().get(Sprint.class, sprintKey);
		for (int key : ticketKey) {
			Ticket ticket = (Ticket)sessionFactory.getCurrentSession().get(Ticket.class, key);
			ticket.setBacklog((Backlog)sessionFactory.getCurrentSession().get(Backlog.class, Backlog.SPRINT_BACKLOG));
			ticket.setSprint(sprint);
			sessionFactory.getCurrentSession().update(ticket);
		}
		sessionFactory.getCurrentSession().flush();
		return "redirect:/ticket/listarPorSprint.action?sprintKey=" + sprintKey;
	}
}
