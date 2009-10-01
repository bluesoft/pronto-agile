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
import br.com.bluesoft.pronto.dao.ClienteDao;
import br.com.bluesoft.pronto.dao.KanbanStatusDao;
import br.com.bluesoft.pronto.dao.TicketDao;
import br.com.bluesoft.pronto.model.Classificacao;
import br.com.bluesoft.pronto.model.Ticket;
import br.com.bluesoft.pronto.model.TicketOrdem;
import br.com.bluesoft.pronto.service.Seguranca;

@Controller
public class BuscarController {

	@Autowired
	private TicketDao ticketDao;

	@Autowired
	private ClienteDao clienteDao;

	@Autowired
	private KanbanStatusDao kanbanStatusDao;

	@RequestMapping("/buscar.action")
	public String buscar(final Model model, final String query, final Integer kanbanStatusKey, final Integer clienteKey, final String ordem, final String classificacao) throws SegurancaException {

		Seguranca.validarPermissao(Papel.PRODUCT_OWNER, Papel.EQUIPE, Papel.SCRUM_MASTER);

		if (NumberUtils.toInt(query) > 0) {
			return "redirect:/ticket/editar.action?ticketKey=" + query;
		}

		TicketOrdem ticketOrdem = TicketOrdem.TITULO;

		if (ordem != null && ordem.length() > 0) {
			ticketOrdem = TicketOrdem.valueOf(ordem);
		}

		Classificacao ticketClassificacao = Classificacao.ASCENDENTE;
		if (classificacao != null && classificacao.length() > 0) {
			ticketClassificacao = Classificacao.valueOf(classificacao);
		}

		final List<Ticket> tickets = ticketDao.buscar(query, kanbanStatusKey, clienteKey, ticketOrdem, ticketClassificacao);
		model.addAttribute("tickets", tickets);
		model.addAttribute("query", query);
		model.addAttribute("kanbanStatusKey", kanbanStatusKey);
		model.addAttribute("clienteKey", clienteKey);
		model.addAttribute("kanbanStatus", kanbanStatusDao.listar());
		model.addAttribute("clientes", clienteDao.listar());
		model.addAttribute("ordens", TicketOrdem.values());
		model.addAttribute("ordem", ticketOrdem);
		model.addAttribute("classificacoes", Classificacao.values());
		model.addAttribute("classificacao", ticketClassificacao);

		return "/buscar/buscar.resultado.jsp";

	}
}
