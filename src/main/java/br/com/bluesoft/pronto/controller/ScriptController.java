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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.dao.BancoDeDadosDao;
import br.com.bluesoft.pronto.dao.ScriptDao;
import br.com.bluesoft.pronto.model.BancoDeDados;
import br.com.bluesoft.pronto.model.Script;

@Controller
public class ScriptController {

	private static final String VIEW_LISTAR = "/script/script.listar.jsp";
	private static final String VIEW_EDITAR = "/script/script.editar.jsp";

	@Autowired
	private ScriptDao scriptDao;

	@Autowired
	private BancoDeDadosDao bancoDeDadosDao;

	@RequestMapping("/script/listar.action")
	public String listar(final Model model) {

		final List<Script> scripts = scriptDao.listarPendentes();
		model.addAttribute("scripts", scripts);

		return VIEW_LISTAR;
	}

	@RequestMapping("/script/editar.action")
	public String editar(final Model model, final int scriptKey) {

		final Script script = scriptDao.obter(scriptKey);
		final List<BancoDeDados> bancos = bancoDeDadosDao.listar();

		model.addAttribute("script", script);
		model.addAttribute("bancos", bancos);

		return VIEW_EDITAR;

	}

	@RequestMapping("/script/incluir.action")
	public String incluir(final Model model) {
		model.addAttribute("script", new Script());
		return VIEW_EDITAR;
	}

	@RequestMapping("/script/salvar.action")
	public String salvar(final Model model, final Script script, final Integer[] bancoDeDadosKey) {

		if (script.getScriptKey() > 0) {

			final Script scriptOriginal = scriptDao.obter(script.getScriptKey());
			scriptOriginal.setDescricao(script.getDescricao());
			scriptOriginal.setScript(script.getScript());

			final Iterator<BancoDeDados> iterator = scriptOriginal.getBancosDeDados().iterator();

			if (bancoDeDadosKey != null) {
				while (iterator.hasNext()) {
					final BancoDeDados bancoDeDados = iterator.next();
					final List<Integer> bancos = Arrays.asList(bancoDeDadosKey);
					if (!bancos.contains(bancoDeDados.getBancoDeDadosKey())) {
						scriptDao.removerExecucao(scriptOriginal.getExecucaoByBancoDeDados(bancoDeDados.getBancoDeDadosKey()));
					}
				}

				for (final Integer banco : bancoDeDadosKey) {
					if (!scriptOriginal.temExecucaoParaOBancoDeDados(banco)) {
						scriptOriginal.adicionarExecucaoParaOBanco(bancoDeDadosDao.obter(banco));
					}
				}
			} else {
				scriptDao.removerExecucoesDoScript(scriptOriginal);
			}

			scriptDao.salvar(scriptOriginal);
		} else {
			scriptDao.salvar(script);
		}

		return "redirect:listar.action";
	}

	@RequestMapping("/script/excluir.action")
	public String excluir(final Model model, final int scriptKey) {
		scriptDao.excluir(scriptDao.obter(scriptKey));
		return "redirect:listar.action";
	}

}
