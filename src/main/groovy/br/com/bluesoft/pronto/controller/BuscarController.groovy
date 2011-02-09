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

package br.com.bluesoft.pronto.controller

import java.util.List

import org.apache.commons.lang.math.NumberUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping

import br.com.bluesoft.pronto.SegurancaException
import br.com.bluesoft.pronto.core.Papel
import br.com.bluesoft.pronto.dao.ClienteDao
import br.com.bluesoft.pronto.dao.KanbanStatusDao
import br.com.bluesoft.pronto.dao.TicketDao
import br.com.bluesoft.pronto.model.Classificacao
import br.com.bluesoft.pronto.model.Ticket
import br.com.bluesoft.pronto.model.TicketOrdem
import br.com.bluesoft.pronto.service.Seguranca

@Controller
@RequestMapping("/buscar")
class BuscarController {
	
	@Autowired TicketDao ticketDao
	
	@Autowired ClienteDao clienteDao
	
	@Autowired KanbanStatusDao kanbanStatusDao
	
	@RequestMapping("/")
	String buscarRest(Model model, String query,  Integer kanbanStatusKey,  Integer clienteKey,  String ordem,  String classificacao, String sprintNome, Boolean ignorarLixeira) {
		
		Seguranca.validarPermissao Papel.PRODUCT_OWNER, Papel.EQUIPE, Papel.SCRUM_MASTER
		
		if (query != null) {
			if (NumberUtils.toInt(query) > 0) {
				return "redirect:/tickets/${query}"
			}
			
			if(query.length() < 2 && sprintNome.length() < 2) {
				model.addAttribute("erro", 'Informe uma palavra para efetuar a busca')
				return "/buscar/buscar.resultado.jsp"
			}
		}

		TicketOrdem ticketOrdem = TicketOrdem.TITULO

		if (ordem != null && ordem.length() > 0) {
			ticketOrdem = TicketOrdem.valueOf(ordem)
		}
		
		Classificacao ticketClassificacao = Classificacao.ASCENDENTE
		if (classificacao != null && classificacao.length() > 0) {
			ticketClassificacao = Classificacao.valueOf(classificacao)
		}
		
		def tickets = ticketDao.buscar(query, kanbanStatusKey, clienteKey, ticketOrdem, ticketClassificacao, sprintNome, ignorarLixeira)
		model.addAttribute("tickets", tickets)
		model.addAttribute("query", query)
		model.addAttribute("kanbanStatusKey", kanbanStatusKey)
		model.addAttribute("clienteKey", clienteKey)
		model.addAttribute("kanbanStatus", kanbanStatusDao.listar())
		model.addAttribute("clientes", clienteDao.listar())
		model.addAttribute("ordens", TicketOrdem.values())
		model.addAttribute("ordem", ticketOrdem)
		model.addAttribute("sprintNome", sprintNome)
		model.addAttribute("classificacoes", Classificacao.values())
		model.addAttribute("classificacao", ticketClassificacao)
		model.addAttribute("ignorarLixeira", ignorarLixeira)
		
		"/buscar/buscar.resultado.jsp"
	}
	
}
