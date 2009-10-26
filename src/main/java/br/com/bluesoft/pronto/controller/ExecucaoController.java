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

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.SegurancaException;
import br.com.bluesoft.pronto.core.Papel;
import br.com.bluesoft.pronto.dao.BancoDeDadosDao;
import br.com.bluesoft.pronto.dao.ExecucaoDao;
import br.com.bluesoft.pronto.model.BancoDeDados;
import br.com.bluesoft.pronto.model.Execucao;
import br.com.bluesoft.pronto.service.Seguranca;

import com.google.common.collect.Lists;

@Controller
public class ExecucaoController {

	private static final String VIEW_LISTAR = "/execucao/execucao.listar.jsp";
	private static final String VIEW_EXECUCAO = "/execucao/execucao.script.jsp";

	@Autowired
	private BancoDeDadosDao bancoDeDadosDao;

	@Autowired
	private ExecucaoDao execucaoDao;

	@RequestMapping("/execucao/listar.action")
	public String listar(final Model model, final Integer bancoDeDadosKey, final Boolean pendentes) throws SegurancaException {

		Seguranca.validarPermissao(Papel.EQUIPE);

		final List<BancoDeDados> bancosComExecucoes = Lists.newArrayList();

		if (bancoDeDadosKey != null) {
			bancosComExecucoes.add(bancoDeDadosDao.obter(bancoDeDadosKey));
		} else {
			bancosComExecucoes.addAll(bancoDeDadosDao.listar());
		}

		model.addAttribute("bancosComExecucoes", bancosComExecucoes);
		model.addAttribute("bancos", bancoDeDadosDao.listar());
		model.addAttribute("pendentes", pendentes == null ? true : pendentes);
		model.addAttribute("bancoDeDadosKey", bancoDeDadosKey);

		return VIEW_LISTAR;
	}

	@RequestMapping("/execucao/gerarScript.action")
	public String listar(final Model model, final Integer bancoDeDadosKey, final Integer[] execucaoKey) throws SegurancaException {

		Seguranca.validarPermissao(Papel.EQUIPE);

		final StringBuilder script = new StringBuilder();

		final List<Execucao> execucoes = execucaoDao.listar(execucaoKey);
		for (final Execucao execucao : execucoes) {
			script.append("/*").append(org.apache.commons.lang.StringUtils.center("", 80, '-')).append("*/").append("\n");
			script.append("/*").append(org.apache.commons.lang.StringUtils.center(" " + execucao.getScript().getDescricao() + " ", 80, ' ')).append("*/").append("\n");
			script.append("/*").append(org.apache.commons.lang.StringUtils.center("", 80, '-')).append("*/").append("\n");
			script.append(execucao.getScript().getScript()).append("\n\n\n");
		}

		model.addAttribute("script", script.toString());
		model.addAttribute("execucaoKey", execucaoKey);
		model.addAttribute("bancoDeDadosKey", bancoDeDadosKey);

		return VIEW_EXECUCAO;

	}

	@RequestMapping("/execucao/confirmar.action")
	public String confirmar(final Model model, final Integer bancoDeDadosKey, final Integer[] execucaoKey) throws SegurancaException {

		Seguranca.validarPermissao(Papel.EQUIPE);

		final StringBuilder script = new StringBuilder();

		final Date agora = new Date();

		final List<Execucao> execucoes = execucaoDao.listar(execucaoKey);
		for (final Execucao execucao : execucoes) {
			execucao.setUsuario(Seguranca.getUsuario());
			execucao.setData(agora);
			execucaoDao.salvar(execucao);
		}

		model.addAttribute("script", script.toString());
		model.addAttribute("execucaoKey", execucaoKey);

		return "redirect:listar.action?bancoDedadosKey=" + bancoDeDadosKey;

	}

}
