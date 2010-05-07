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

import br.com.bluesoft.pronto.SegurancaException
import br.com.bluesoft.pronto.core.Papel
import br.com.bluesoft.pronto.dao.BancoDeDadosDao
import br.com.bluesoft.pronto.model.BancoDeDados
import br.com.bluesoft.pronto.service.Seguranca

@Controller
@RequestMapping("/bancosDeDados")
class BancoDeDadosController {

	private static final String VIEW_LISTAR = "/bancoDeDados/bancoDeDados.listar.jsp"
	private static final String VIEW_EDITAR = "/bancoDeDados/bancoDeDados.editar.jsp"

	@Autowired private BancoDeDadosDao bancoDeDadosDao

	@RequestMapping(method = GET)
	String listar(Model model) {
		Seguranca.validarPermissao Papel.EQUIPE
		model.addAttribute("bancos", bancoDeDadosDao.listar())
		VIEW_LISTAR
	}

	@RequestMapping("/{bancoDeDadosKey}")
	String editar(Model model, @PathVariable int bancoDeDadosKey) {
		Seguranca.validarPermissao Papel.EQUIPE
		model.addAttribute("bancoDeDados", bancoDeDadosDao.obter(bancoDeDadosKey))
		VIEW_EDITAR
	}

	@RequestMapping("/novo")
	String incluir(Model model) throws SegurancaException {
		Seguranca.validarPermissao Papel.EQUIPE
		model.addAttribute "bancoDeDados", new BancoDeDados()
		VIEW_EDITAR
	}

	@RequestMapping(method=[PUT, POST])
	String salvar(Model model, BancoDeDados bancoDeDados)  {
		Seguranca.validarPermissao Papel.EQUIPE
		bancoDeDadosDao.salvar bancoDeDados
		"redirect:/bancosDeDados"
	}

	@RequestMapping(value="/{bancoDeDadosKey}", method = DELETE)
	String excluir(Model model, @PathVariable int bancoDeDadosKey) {
		Seguranca.validarPermissao Papel.EQUIPE
		bancoDeDadosDao.excluir bancoDeDadosDao.obter(bancoDeDadosKey)
		"redirect:/bancosDeDados?mensagem=Banco de dados excluído com sucesso."
	}

}
