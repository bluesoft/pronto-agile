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
 * but WITHOUT ANY WARRANTY without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Pronto. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package br.com.bluesoft.pronto.controller

import static org.springframework.web.bind.annotation.RequestMethod.*


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

import br.com.bluesoft.pronto.core.Papel
import br.com.bluesoft.pronto.dao.ClienteDao
import br.com.bluesoft.pronto.dao.TicketDao
import br.com.bluesoft.pronto.model.Cliente
import br.com.bluesoft.pronto.service.Seguranca

@Controller
@RequestMapping("/clientes")
public class ClienteController {
	
	private static final String VIEW_LISTAR = "/cliente/cliente.listar.jsp"
	private static final String VIEW_EDITAR = "/cliente/cliente.editar.jsp"
	private static final String VIEW_BACKLOG = "/cliente/cliente.backlog.jsp"
	
	@Autowired private ClienteDao clienteDao
	
	@Autowired private TicketDao ticketDao
	
	@RequestMapping(method = GET)
	String listar(Model model) {
		Seguranca.validarPermissao Papel.PRODUCT_OWNER
		model.addAttribute "clientes", clienteDao.listar()
		VIEW_LISTAR
	}
	
	@RequestMapping(value = "/novo", method = GET)
	String novo(final Model model)  {
		Seguranca.validarPermissao Papel.PRODUCT_OWNER
		model.addAttribute "cliente", new Cliente()
		VIEW_EDITAR
	}
	
	@RequestMapping(value = "/{clienteKey}", method = GET)
	String editar(final Model model, @PathVariable final Integer clienteKey) {
		Seguranca.validarPermissao Papel.PRODUCT_OWNER
		model.addAttribute "cliente", clienteDao.obter(clienteKey)
		VIEW_EDITAR
	}
	
	@RequestMapping(value = "/{clienteKey}", method = DELETE)
	String excluir(final Model model, @PathVariable int clienteKey) {
		Seguranca.validarPermissao Papel.PRODUCT_OWNER
		
		def cliente = clienteDao.obter(clienteKey)
		try {
			clienteDao.excluir(cliente)
			model.addAttribute("mensagem", "Cliente excluído com sucesso.")
		} catch (e) {
			model.addAttribute("erro", "Este cliente não pode ser excluído porque existem tarefas e/ou usuários vinculados a ele.")
		}
		
		return "redirect:/clientes"
	}
	
	@RequestMapping(method = [ POST, PUT ])
	String salvar(final Model model, final Cliente cliente) {
		Seguranca.validarPermissao Papel.PRODUCT_OWNER
		clienteDao.salvar(cliente)
		return "redirect:/clientes?mensagem=Cliente salvo com sucesso."
	}
	
	@RequestMapping(value = "/backlog", method = GET)
	String backlog(final Model model)  {
		
		Seguranca.validarPermissao Papel.CLIENTE
		
		def cliente = Seguranca.usuario.cliente
		model.addAttribute 'tickets', ticketDao.listarPorCliente(cliente.clienteKey)
		model.addAttribute 'cliente', cliente
		
		return VIEW_BACKLOG
	}
	
	@RequestMapping("/priorizar")
	String priorizar(final Integer[] ticketKey)  {
		Seguranca.validarPermissao Papel.CLIENTE
		def cliente = Seguranca.usuario.cliente
		ticketDao.alterarPrioridadeDoCliente(cliente.clienteKey, ticketKey)
		"redirect:${VIEW_BACKLOG}"  
	}
	
}
