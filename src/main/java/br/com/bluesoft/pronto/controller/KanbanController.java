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

import java.util.Date;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.core.KanbanStatus;
import br.com.bluesoft.pronto.dao.SprintDao;
import br.com.bluesoft.pronto.dao.TicketDao;
import br.com.bluesoft.pronto.model.Sprint;
import br.com.bluesoft.pronto.model.Ticket;
import br.com.bluesoft.pronto.service.Seguranca;

@Controller
public class KanbanController {

	private static final String VIEW_KANBAN = "/kanban/kanban.kanban.jsp";

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private SprintDao sprintDao;

	@Autowired
	private TicketDao ticketDao;

	@RequestMapping("/kanban/kanban.action")
	public String index(final Model model, Sprint sprint) {

		if (sprint == null || sprint.getSprintKey() == 0) {
			sprint = sprintDao.getSprintAtualComTickets();
		} else {
			sprint = sprintDao.obterSprintComTicket(sprint.getSprintKey());
		}

		if (sprint == null) {
			return LoginController.VIEW_BEM_VINDO;
		}

		model.addAttribute("sprint", sprint);
		model.addAttribute("tickets", sprint.getTicketsParaOKanban());
		model.addAttribute("sprints", sprintDao.listarSprintsEmAberto());

		return VIEW_KANBAN;
	}

	@RequestMapping("/kanban/mover.action")
	public String mover(final Model model, final int ticketKey, final int kanbanStatusKey) {

		final Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey);
		ticket.setKanbanStatus((KanbanStatus) sessionFactory.getCurrentSession().get(KanbanStatus.class, kanbanStatusKey));

		if (kanbanStatusKey == KanbanStatus.DONE) {
			ticket.setDataDePronto(new Date());
			if (Seguranca.getUsuario().isTestador()) {
				ticket.addTestador(Seguranca.getUsuario());
			}
		} else {
			ticket.setDataDePronto(null);
			if (kanbanStatusKey == KanbanStatus.DOING && Seguranca.getUsuario().isDesenvolvedor()) {
				ticket.addDesenvolvedor(Seguranca.getUsuario());
			}
		}

		ticketDao.salvar(ticket);

		return null;
	}

}
