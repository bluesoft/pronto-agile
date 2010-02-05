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

import java.util.Date
import java.util.List

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping

import br.com.bluesoft.pronto.SegurancaException
import br.com.bluesoft.pronto.core.Papel
import br.com.bluesoft.pronto.dao.BancoDeDadosDao
import br.com.bluesoft.pronto.dao.ExecucaoDao
import br.com.bluesoft.pronto.model.BancoDeDados
import br.com.bluesoft.pronto.model.Execucao
import br.com.bluesoft.pronto.service.Seguranca

import com.google.common.collect.Lists
import static org.springframework.web.bind.annotation.RequestMethod.*

@Controller
@RequestMapping("/execucoes")
class ExecucaoController {
	
	private static final String VIEW_LISTAR = "/execucao/execucao.listar.jsp"
	private static final String VIEW_EXECUCAO = "/execucao/execucao.script.jsp"
	
	@Autowired BancoDeDadosDao bancoDeDadosDao
	
	@Autowired ExecucaoDao execucaoDao
	
	@RequestMapping(method = GET)
	String listarTodos(Model model) {
		listar model, null, false
	}

	@RequestMapping(value="/pendentes", method = GET)
	String listarPendentes(Model model) {
		listar model, null, true
	}

	@RequestMapping(value= '/{bancoDeDadosKey}/pendentes', method = GET)
	String listarPendentesBancoDeDados( Model model,  @PathVariable Integer bancoDeDadosKey) {
		listar model, bancoDeDadosKey, true
	}
	
	@RequestMapping(value= '/{bancoDeDadosKey}', method = GET)
	String listarPorBancoDeDados( Model model,  @PathVariable Integer bancoDeDadosKey) {
		listar model, bancoDeDadosKey, false
	}
	
	private String listar( Model model,  Integer bancoDeDadosKey,  Boolean pendentes) {
		
		Seguranca.validarPermissao Papel.EQUIPE
		
		def bancosComExecucoes = []
		
		if (bancoDeDadosKey != null) {
			bancosComExecucoes.add(bancoDeDadosDao.obter(bancoDeDadosKey))
		} else {
			bancosComExecucoes.addAll(bancoDeDadosDao.listar())
		}
		
		model.addAttribute "bancosComExecucoes", bancosComExecucoes
		model.addAttribute "bancos", bancoDeDadosDao.listar()
		model.addAttribute "pendentes", pendentes == null ? true : pendentes
		model.addAttribute "bancoDeDadosKey", bancoDeDadosKey
		
		VIEW_LISTAR
	}
	
	
	@RequestMapping(value="/gerar", method=POST)
	String gerar( Model model,  Integer bancoDeDadosKey,  Integer[] execucaoKey)  {
		
		Seguranca.validarPermissao(Papel.EQUIPE)
		
		StringBuilder script = new StringBuilder()
		
		List<Execucao> execucoes = execucaoDao.listar(execucaoKey)
		for ( Execucao execucao : execucoes) {
			script.append("/*").append(org.apache.commons.lang.StringUtils.center("", 80, '-')).append("*/").append("\n")
			script.append("/*").append(org.apache.commons.lang.StringUtils.center(" " + execucao.getScript().getDescricao() + " ", 80, ' ')).append("*/").append("\n")
			script.append("/*").append(org.apache.commons.lang.StringUtils.center("", 80, '-')).append("*/").append("\n")
			script.append(execucao.getScript().getScript()).append("\n\n\n")
		}
		
		model.addAttribute 'script', script.toString()
		model.addAttribute 'execucaoKey', execucaoKey
		model.addAttribute 'bancoDeDadosKey', bancoDeDadosKey
		
		VIEW_EXECUCAO
		
	}
	
	@RequestMapping(method = [ POST, PUT ])
	String confirmar( Model model,  Integer bancoDeDadosKey,  Integer[] execucaoKey) {
		
		Seguranca.validarPermissao Papel.EQUIPE
		
		def agora = new Date()
		def execucoes = execucaoDao.listar(execucaoKey)
		execucoes.each() { Execucao it ->
			it.usuario = Seguranca.usuario
			it.data = agora
			execucaoDao.salvar it
		}
		
		model.addAttribute "execucaoKey", execucaoKey
		
		"redirect:/execucoes/${bancoDeDadosKey}"
	}
	
}
