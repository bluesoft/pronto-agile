package br.com.bluesoft.pronto.controller;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.model.Ticket;

@Controller
public class TicketController {

	private static final String VIEW_SALVAR = "ticket.listar.jsp";
	private static final String VIEW_EDITAR = "ticket.editar.jsp";

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@RequestMapping("/ticket/ticket.listar.action")
	public String listar(final Model model) {
		final List<Ticket> tickets = sessionFactory.getCurrentSession().createCriteria(Ticket.class).list();
		model.addAttribute("tickets", tickets);
		return VIEW_SALVAR;
	}

	@RequestMapping("/ticket/ticket.salvar.action")
	public String salvar(final Model model) {
		return VIEW_SALVAR;
	}

	@RequestMapping("/ticket/ticket.editar.action")
	public String editar(final Model model, final Integer ticketKey) {
		if (ticketKey != null) {
			final Ticket ticket = (Ticket) sessionFactory.getCurrentSession().createCriteria(Ticket.class).add(Restrictions.eq("ticketKey", ticketKey)).uniqueResult();
			model.addAttribute("ticket", ticket);
		} else {
			model.addAttribute("ticket", new Ticket());
		}
		return VIEW_EDITAR;
	}
}
