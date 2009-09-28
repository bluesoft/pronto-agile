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

import br.com.bluesoft.pronto.SegurancaException;
import br.com.bluesoft.pronto.core.Papel;
import br.com.bluesoft.pronto.dao.BancoDeDadosDao;
import br.com.bluesoft.pronto.model.BancoDeDados;
import br.com.bluesoft.pronto.service.Seguranca;

@Controller
public class BancoDeDadosController {

	private static final String VIEW_LISTAR = "/bancoDeDados/bancoDeDados.listar.jsp";
	private static final String VIEW_EDITAR = "/bancoDeDados/bancoDeDados.editar.jsp";

	@Autowired
	private BancoDeDadosDao bancoDeDadosDao;

	@RequestMapping("/bancoDeDados/listar.action")
	public String listar(final Model model) throws SegurancaException {
		Seguranca.validarPermissao(Papel.EQUIPE);
		final List<BancoDeDados> bancos = bancoDeDadosDao.listar();
		model.addAttribute("bancos", bancos);
		return VIEW_LISTAR;
	}

	@RequestMapping("/bancoDeDados/editar.action")
	public String editar(final Model model, final int bancoDeDadosKey) throws SegurancaException {
		Seguranca.validarPermissao(Papel.EQUIPE);
		final BancoDeDados bancoDeDados = bancoDeDadosDao.obter(bancoDeDadosKey);
		model.addAttribute("bancoDeDados", bancoDeDados);
		return VIEW_EDITAR;
	}

	@RequestMapping("/bancoDeDados/incluir.action")
	public String incluir(final Model model) throws SegurancaException {
		Seguranca.validarPermissao(Papel.EQUIPE);
		model.addAttribute("bancoDeDados", new BancoDeDados());
		return VIEW_EDITAR;
	}

	@RequestMapping("/bancoDeDados/salvar.action")
	public String salvar(final Model model, final BancoDeDados bancoDeDados) throws SegurancaException {
		Seguranca.validarPermissao(Papel.EQUIPE);
		bancoDeDadosDao.salvar(bancoDeDados);
		return "redirect:listar.action";
	}

	@RequestMapping("/bancoDeDados/excluir.action")
	public String excluir(final Model model, final int bancoDeDadosKey) throws SegurancaException {
		Seguranca.validarPermissao(Papel.EQUIPE);
		final BancoDeDados bancoDeDados = bancoDeDadosDao.obter(bancoDeDadosKey);
		bancoDeDadosDao.excluir(bancoDeDados);
		return "redirect:listar.action";
	}

}
