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

import java.text.SimpleDateFormat

import org.apache.commons.lang.math.NumberUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.propertyeditors.CustomDateEditor
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.context.request.WebRequest

import br.com.bluesoft.pronto.core.Papel
import br.com.bluesoft.pronto.dao.BacklogDao
import br.com.bluesoft.pronto.dao.CategoriaDao
import br.com.bluesoft.pronto.dao.ClienteDao
import br.com.bluesoft.pronto.dao.KanbanStatusDao
import br.com.bluesoft.pronto.dao.ModuloDao
import br.com.bluesoft.pronto.dao.TicketDao
import br.com.bluesoft.pronto.dao.TipoDeTicketDao
import br.com.bluesoft.pronto.dao.UsuarioDao
import br.com.bluesoft.pronto.model.Classificacao
import br.com.bluesoft.pronto.model.TicketOrdem
import br.com.bluesoft.pronto.service.Seguranca
import br.com.bluesoft.pronto.to.TicketFilter
import br.com.bluesoft.pronto.web.binding.SqlDateEditor

@Controller
@RequestMapping("/buscar")
class BuscarController {
	
	@Autowired TicketDao ticketDao
	
	@Autowired ClienteDao clienteDao
	
	@Autowired UsuarioDao usuarioDao
	
	@Autowired KanbanStatusDao kanbanStatusDao
	
	@Autowired BacklogDao backlogDao
	
	@Autowired CategoriaDao categoriaDao
	
	@Autowired ModuloDao moduloDao
	
	@Autowired TipoDeTicketDao tipoDeTicketDao
	
	@RequestMapping("/")
	String buscarRest(Model model, TicketFilter filtro) {
		
		Seguranca.validarPermissao Papel.PRODUCT_OWNER, Papel.EQUIPE, Papel.SCRUM_MASTER
		
		if (filtro.query != null) {
			if (NumberUtils.toInt(filtro.query) > 0) {
				return "redirect:/tickets/${query}"
			} 
		} 
		
		if (filtro.temCriterios()) {
			def tickets = ticketDao.buscar(filtro)
			model.addAttribute("tickets", tickets)
		}
		model.addAttribute("classificacoes", Classificacao.values())
		model.addAttribute("ordens", TicketOrdem.values())
		model.addAttribute("kanbanStatus", kanbanStatusDao.listar())
		model.addAttribute("clientes", clienteDao.listar())
		model.addAttribute("usuarios", usuarioDao.listarOrdenadoPorNome())
		model.addAttribute("categorias", categoriaDao.listar())
		model.addAttribute("modulos", moduloDao.listar())
		model.addAttribute("backlogs", backlogDao.listar())
		model.addAttribute("tiposDeTicket", tipoDeTicketDao.listar())
		model.addAttribute("filtro", filtro)
		
		"/buscar/buscar.resultado.jsp"
	}
	
	@InitBinder
	public void initBinder(final WebDataBinder binder, final WebRequest webRequest) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(java.sql.Date.class, new SqlDateEditor(dateFormat, true));
	}
	
}
