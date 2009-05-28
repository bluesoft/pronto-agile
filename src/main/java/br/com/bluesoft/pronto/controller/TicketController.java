package br.com.bluesoft.pronto.controller;

import java.util.List;

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
	public String salvar(final Model model, final Ticket ticket) {
		final Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		sessionFactory.getCurrentSession().saveOrUpdate(ticket);
		sessionFactory.getCurrentSession().flush();
		tx.commit();
		
		return "redirect:listarPorBacklog.action?backlogKey=" + ticket.getBacklog().getBacklogKey();
	}

	@RequestMapping("/ticket/editar.action")
	public String editar(final Model model, final Integer ticketKey, final Integer tipoDeTicketKey, final Integer backlogKey) {
		if (ticketKey != null) {
			final Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey);
			model.addAttribute("ticket", ticket);
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
}
