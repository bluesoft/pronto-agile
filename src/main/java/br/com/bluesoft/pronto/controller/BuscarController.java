package br.com.bluesoft.pronto.controller;

import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.dao.TicketDao;
import br.com.bluesoft.pronto.model.Ticket;

@Controller
public class BuscarController {

	@Autowired
	private TicketDao ticketDao;

	@RequestMapping("/buscar.action")
	public String buscar(final Model model, final String query) {

		if (NumberUtils.toInt(query) > 0) {
			return "redirect:/ticket/editar.action?ticketKey=" + query;
		}

		final List<Ticket> tickets = ticketDao.buscar(query);
		model.addAttribute("tickets", tickets);
		return "/buscar/buscar.resultado.jsp";

	}

}
