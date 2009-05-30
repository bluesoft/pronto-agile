package br.com.bluesoft.pronto.controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.core.Backlog;
import br.com.bluesoft.pronto.core.TipoDeTicket;
import br.com.bluesoft.pronto.model.Sprint;
import br.com.bluesoft.pronto.model.Ticket;
import br.com.bluesoft.pronto.model.Usuario;

@Controller
public class TicketController {

	private static final String VIEW_LISTAR = "ticket.listar.jsp";
	private static final String VIEW_EDITAR = "ticket.editar.jsp";
	private static String dirBasePath = "c:/images/";

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
		final List<Ticket> tickets = sessionFactory.getCurrentSession().createCriteria(Ticket.class).add(Restrictions.eq("backlog.backlogKey", backlogKey)).list();
		model.addAttribute("tickets", tickets);
		model.addAttribute("backlog", sessionFactory.getCurrentSession().get(Backlog.class, backlogKey));
		return VIEW_LISTAR;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/ticket/listarPorSprint.action")
	public String listarPorSprint(final Model model, int sprintKey) {
		final List<Ticket> tickets = sessionFactory.getCurrentSession().createCriteria(Ticket.class).add(Restrictions.eq("sprint.sprintKey", sprintKey)).list();
		model.addAttribute("tickets", tickets);
		model.addAttribute("sprint", sessionFactory.getCurrentSession().get(Sprint.class, sprintKey));
		return VIEW_LISTAR;
	}

	@RequestMapping("/ticket/salvar.action")
	public String salvar(final Model model, final Ticket ticket, String comentario) {
		final Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		ticket.setBacklog((Backlog) sessionFactory.getCurrentSession().get(Backlog.class, ticket.getBacklog().getBacklogKey()));
		ticket.setTipoDeTicket((TipoDeTicket) sessionFactory.getCurrentSession().get(TipoDeTicket.class, ticket.getTipoDeTicket().getTipoDeTicketKey()));
		ticket.setReporter((Usuario) sessionFactory.getCurrentSession().get(Usuario.class, ticket.getReporter().getUsuarioKey()));

		if (comentario != null && comentario.trim().length() > 0) {
			ticket.addComentario(comentario, "andrefaria");
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
			model.addAttribute("ticket", ticket);
			model.addAttribute("anexos", listarAnexos(ticketKey));
		} else {
			final Ticket novoTicket = new Ticket();
			novoTicket.setReporter(LoginFilter.getUsuarioAtual());
			novoTicket.setTipoDeTicket((TipoDeTicket) sessionFactory.getCurrentSession().get(TipoDeTicket.class, tipoDeTicketKey));
			novoTicket.setBacklog((Backlog) sessionFactory.getCurrentSession().get(Backlog.class, backlogKey));
			model.addAttribute("ticket", novoTicket);
			model.addAttribute("tipoDeTicketKey", tipoDeTicketKey);
		}
		model.addAttribute("usuarios", sessionFactory.getCurrentSession().createCriteria(Usuario.class).list());
		return VIEW_EDITAR;
	}

	@RequestMapping("/ticket/jogarNoLixo.action")
	public String jogarNoLixo(Model model, int ticketKey, HttpServletResponse response) {
		Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey);
		ticket.setBacklog((Backlog) sessionFactory.getCurrentSession().get(Backlog.class, Backlog.LIXEIRA));
		sessionFactory.getCurrentSession().update(ticket);
		sessionFactory.getCurrentSession().flush();
		return null;
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
		return null;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/ticket/upload.action")
	public String upload(HttpServletRequest request, int ticketKey) throws Exception {

		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);

		List<FileItem> items = upload.parseRequest(request);
		String ticketDir = dirBasePath + ticketKey + "/";
		File dir = new File(ticketDir);
		dir.mkdirs();

		for (FileItem fileItem : items) {
			fileItem.write(new File(ticketDir + fileItem.getName()));
		}

		return "redirect:editar.action?ticketKey=" + ticketKey;
	}

	private List<String> listarAnexos(int ticketKey) {
		File file = new File(dirBasePath + ticketKey);
		if (file.exists()) {
			return (Arrays.asList(file.list()));
		} else {
			return null;
		}
	}

	@RequestMapping("/ticket/excluirAnexo.action")
	public String excluirAnexo(String file, int ticketKey) throws Exception {
		File arquivo = new File(dirBasePath + ticketKey + "/" + file);
		if (arquivo.exists()) {
			arquivo.delete();
		}
		return "redirect:editar.action?ticketKey=" + ticketKey;
	}

	@RequestMapping("/ticket/download.action")
	public String download(HttpServletResponse response, String file, int ticketKey) throws Exception {

		File arquivo = new File(dirBasePath + ticketKey + "/" + file);
		FileInputStream fis = new FileInputStream(arquivo);
		int numberBytes = fis.available();
		byte bytes[] = new byte[numberBytes];
		fis.read(bytes);
		fis.close();

		response.getOutputStream().write(bytes);
		response.setHeader("Content-disposition", "attachment;filename=" + file);

		return null;

	}

}
