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

import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.SegurancaException;
import br.com.bluesoft.pronto.core.Papel;
import br.com.bluesoft.pronto.dao.TicketDao;
import br.com.bluesoft.pronto.model.Ticket;
import br.com.bluesoft.pronto.service.Seguranca;

@Controller
public class BuscarController {

	@Autowired
	private TicketDao ticketDao;

	@RequestMapping("/buscar.action")
	public String buscar(final Model model, final String query) throws SegurancaException {

		Seguranca.validarPermissao(Papel.PRODUCT_OWNER, Papel.EQUIPE, Papel.SCRUM_MASTER);

		if (NumberUtils.toInt(query) > 0) {
			return "redirect:/ticket/editar.action?ticketKey=" + query;
		}

		final List<Ticket> tickets = ticketDao.buscar(query);
		model.addAttribute("tickets", tickets);
		return "/buscar/buscar.resultado.jsp";

	}

}
