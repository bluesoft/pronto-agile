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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.ProntoException;
import br.com.bluesoft.pronto.SegurancaException;
import br.com.bluesoft.pronto.core.Papel;
import br.com.bluesoft.pronto.dao.ClienteDao;
import br.com.bluesoft.pronto.dao.TicketDao;
import br.com.bluesoft.pronto.model.Cliente;
import br.com.bluesoft.pronto.model.Ticket;
import br.com.bluesoft.pronto.service.Seguranca;

@Controller
public class ClienteController {

	private static final String VIEW_LISTAR = "cliente.listar.jsp";
	private static final String VIEW_EDITAR = "cliente.editar.jsp";
	private static final String VIEW_BACKLOG = "cliente.backlog.jsp";

	@Autowired
	private ClienteDao clienteDao;

	@Autowired
	private TicketDao ticketDao;

	@RequestMapping("/cliente/listar.action")
	public String listar(final Model model) throws SegurancaException {
		Seguranca.validarPermissao(Papel.ADMINISTRADOR, Papel.PRODUCT_OWNER);
		final List<Cliente> clientes = clienteDao.listar();
		model.addAttribute("clientes", clientes);
		return VIEW_LISTAR;
	}

	@RequestMapping("/cliente/editar.action")
	public String editar(final Model model, final Integer clienteKey) throws ProntoException {
		Seguranca.validarPermissao(Papel.ADMINISTRADOR, Papel.PRODUCT_OWNER);

		if (clienteKey != null && clienteKey > 0) {
			model.addAttribute("cliente", clienteDao.obter(clienteKey));
		} else {
			model.addAttribute("cliente", new Cliente());
		}

		return VIEW_EDITAR;
	}

	@RequestMapping("/cliente/excluir.action")
	public String excluir(final Model model, final int clienteKey) throws ProntoException {
		Seguranca.validarPermissao(Papel.ADMINISTRADOR, Papel.PRODUCT_OWNER);

		final Cliente cliente = clienteDao.obter(clienteKey);
		try {
			clienteDao.excluir(cliente);
			model.addAttribute("mensagem", "Cliente excluido com suceso.");
		} catch (final Exception e) {
			model.addAttribute("erro", "Este cliente não pode ser excluido porque existem tarefas e/ou usuários vinculados a ele.");
		}

		return "forward:listar.action";
	}

	@RequestMapping("/cliente/salvar.action")
	public String salvar(final Model model, final Cliente cliente) throws Exception {
		Seguranca.validarPermissao(Papel.ADMINISTRADOR, Papel.PRODUCT_OWNER);
		clienteDao.salvar(cliente);
		return "forward:listar.action";
	}

	@RequestMapping("/cliente/backlog.action")
	public String backlog(final Model model) throws SegurancaException {

		Seguranca.validarPermissao(Papel.CLIENTE);

		final List<Ticket> tickets = ticketDao.listarPorCliente(Seguranca.getUsuario().getUsername());
		model.addAttribute("tickets", tickets);

		return VIEW_BACKLOG;
	}

}
