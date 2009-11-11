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

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.JavaScriptUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import br.com.bluesoft.pronto.dao.BancoDeDadosDao;
import br.com.bluesoft.pronto.dao.ScriptDao;
import br.com.bluesoft.pronto.dao.TicketDao;
import br.com.bluesoft.pronto.model.BancoDeDados;
import br.com.bluesoft.pronto.model.Script;
import br.com.bluesoft.pronto.model.Ticket;
import br.com.bluesoft.pronto.util.ControllerUtil;

@Controller
public class ScriptController {

	private static final String VIEW_LISTAR = "/script/script.listar.jsp";
	private static final String VIEW_EDITAR = "/script/script.editar.jsp";

	@Autowired
	private ScriptDao scriptDao;

	@Autowired
	private BancoDeDadosDao bancoDeDadosDao;

	@Autowired
	private TicketDao ticketDao;

	@RequestMapping("/script/listar.action")
	public String listar(final Model model, Integer situacao) {

		final List<Script> todosOsScripts = scriptDao.listarComDependencias();

		Iterator<Script> scripts = null;
		if (situacao != null) {
			if (situacao == 0) {
				scripts = todosOsScripts.iterator();
			} else if (situacao == 1) {
				scripts = Iterables.filter(todosOsScripts, filterPendente).iterator();
			} else {
				scripts = Iterables.filter(todosOsScripts, filterExecutado).iterator();
			}
		} else {
			scripts = Iterables.filter(todosOsScripts, filterPendente).iterator();
		}

		model.addAttribute("situacao", situacao);
		model.addAttribute("scripts", scripts);

		return VIEW_LISTAR;
	}

	@RequestMapping("/script/editar.action")
	public String editar(final Model model, final int scriptKey) {
		model.addAttribute("script", scriptDao.obter(scriptKey));
		model.addAttribute("bancos", bancoDeDadosDao.listar());
		return VIEW_EDITAR;

	}

	@RequestMapping("/script/incluir.action")
	public String incluir(final Model model) {
		model.addAttribute("script", new Script());
		model.addAttribute("bancos", bancoDeDadosDao.listar());
		return VIEW_EDITAR;
	}

	@RequestMapping("/script/salvar.action")
	public String salvar(final Model model, final Script script, final Integer[] bancoDeDadosKey, final Integer ticketKey) {

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
			if (bancoDeDadosKey != null) {
				for (final Integer banco : bancoDeDadosKey) {
					script.adicionarExecucaoParaOBanco(bancoDeDadosDao.obter(banco));
				}
			}
			scriptDao.salvar(script);
			if (ticketKey != null) {
				Ticket ticket = ticketDao.obter(ticketKey);
				ticket.setScript(script);
				ticketDao.salvar(ticket);
			}
		}

		return "redirect:listar.action";
	}

	@RequestMapping("/script/excluir.action")
	public String excluir(final Model model, final int scriptKey) {
		final Script script = scriptDao.obter(scriptKey);
		final Ticket ticket = script.getTicket();
		if (ticket != null) {
			ticket.setScript(null);
			ticketDao.salvar(ticket);
		}
		scriptDao.excluir(script);
		return "redirect:listar.action";
	}

	@RequestMapping("/script/verScript.action")
	public void verScript(final HttpServletResponse response, final int scriptKey) {
		final Script script = scriptDao.obter(scriptKey);
		final String json = String.format("{descricao: '%s', script: '%s'}", script.getDescricao(), JavaScriptUtils.javaScriptEscape(script.getScript()));
		ControllerUtil.writeText(response, json);
	}

	private final Predicate<Script> filterPendente = new Predicate<Script>() {

		@Override
		public boolean apply(Script script) {
			return !script.isTudoExecutado();
		}

	};

	private final Predicate<Script> filterExecutado = new Predicate<Script>() {

		@Override
		public boolean apply(Script script) {
			return script.isTudoExecutado();
		}

	};

}
