package br.com.bluesoft.pronto.controller;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.model.Ticket;

@Controller
public class KanbanController {

	private static final String VIEW_KANBAN = "/kanban/kanban.kanban.jsp";

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@RequestMapping("/kanban/kanban.action")
	public String index(Model model) {

		String hql = "from Ticket t inner join fetch t.sprint s where s.atual = true";

		List<Ticket> tickets = sessionFactory.getCurrentSession().createQuery(hql).list();
		model.addAttribute("tickets", tickets);
		return VIEW_KANBAN;
	}

}
