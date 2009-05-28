package br.com.bluesoft.pronto.controller;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.model.Sprint;
import br.com.bluesoft.pronto.model.Usuario;

@Controller
public class SprintController {

	private static final String VIEW_LISTAR = "/sprint/sprint.listar.jsp";
	private static final String VIEW_EDITAR = "/sprint/sprint.editar.jsp";

	@Autowired
	private SessionFactory sessionFactory;

	@RequestMapping("/sprint/listar.action")
	public String listar(Model model) {
		model.addAttribute("sprints", sessionFactory.getCurrentSession().createCriteria(Sprint.class).list());
		return VIEW_LISTAR;
	}

	@RequestMapping("/sprint/editar.action")
	public String editar(final Model model, final Integer sprintKey) {

		if (sprintKey != null) {
			final Sprint sprint = (Sprint) sessionFactory.getCurrentSession().get(Sprint.class, sprintKey);
			model.addAttribute("sprint", sprint);
		} else {
			model.addAttribute("sprint", new Sprint());
		}

		return VIEW_EDITAR;
	}

	@RequestMapping("/sprint/salvar.action")
	public String salvar(final Model model, final Sprint sprint) {
		final Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		sessionFactory.getCurrentSession().saveOrUpdate(sprint);
		sessionFactory.getCurrentSession().flush();
		tx.commit();
		return "forward:listar.action";
	}

}
