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

import java.util.Arrays
import java.util.Iterator
import java.util.List

import javax.servlet.http.HttpServletResponse

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.util.JavaScriptUtils

import br.com.bluesoft.pronto.dao.BancoDeDadosDao
import br.com.bluesoft.pronto.dao.ScriptDao
import br.com.bluesoft.pronto.dao.TicketDao
import br.com.bluesoft.pronto.model.BancoDeDados
import br.com.bluesoft.pronto.model.Script
import br.com.bluesoft.pronto.model.Ticket

import com.google.common.base.Predicate
import com.google.common.collect.Iterables

@Controller
@RequestMapping("/scripts")
class ScriptController {
	
	private static final String VIEW_LISTAR = "/script/script.listar.jsp"
	private static final String VIEW_EDITAR = "/script/script.editar.jsp"
	
	@Autowired private ScriptDao scriptDao
	
	@Autowired private BancoDeDadosDao bancoDeDadosDao
	
	@Autowired private TicketDao ticketDao
	
	@RequestMapping(method = GET)
	String listar( Model model,  Integer situacao) {
		
		def todosOsScripts = scriptDao.listarComDependencias()
		
		Iterator<Script> scripts = null
		if (situacao != null) {
			if (situacao == 0) {
				scripts = todosOsScripts.iterator()
			} else if (situacao == 1) {
				scripts = todosOsScripts.findAll { Script it -> !it.isTudoExecutado() }.iterator()
			} else {
				scripts = todosOsScripts.findAll { Script it -> it.isTudoExecutado() }.iterator()
			}
		} else {
			scripts = todosOsScripts.findAll { Script it -> !it.isTudoExecutado() }.iterator()
		}
		
		model.addAttribute "situacao", situacao
		model.addAttribute "scripts", scripts
		
		return VIEW_LISTAR
	}
	
	@RequestMapping(value= "/{scriptKey}",method=GET)
	String editar( Model model, @PathVariable  int scriptKey) {
		model.addAttribute "script", scriptDao.obter(scriptKey)
		model.addAttribute "bancos", bancoDeDadosDao.listar()
		return VIEW_EDITAR
		
	}
	
	@RequestMapping(value="/novo", method=GET)
	String incluir( Model model) {
		model.addAttribute "script", new Script()
		model.addAttribute "bancos", bancoDeDadosDao.listar()
		return VIEW_EDITAR
	}
	
	@RequestMapping(method = [ POST, PUT ])
	String salvar( Model model,  Script script,  Integer[] bancoDeDadosKey,  Integer ticketKey) {
		
		if (script.scriptKey > 0) {
			
			Script scriptOriginal = scriptDao.obter(script.scriptKey)
			scriptOriginal.setDescricao(script.descricao)
			scriptOriginal.setScript(script.script)
			
			Iterator<BancoDeDados> iterator = scriptOriginal.getBancosDeDados().iterator()
			
			if (bancoDeDadosKey != null) {
				while (iterator.hasNext()) {
					BancoDeDados bancoDeDados = iterator.next()
					List<Integer> bancos = Arrays.asList(bancoDeDadosKey)
					if (!bancos.contains(bancoDeDados.getBancoDeDadosKey())) {
						scriptDao.removerExecucao(scriptOriginal.getExecucaoByBancoDeDados(bancoDeDados.getBancoDeDadosKey()))
					}
				}
				
				
				bancoDeDadosKey.each() { 
					if (!scriptOriginal.temExecucaoParaOBancoDeDados(it)) {
						scriptOriginal.adicionarExecucaoParaOBanco(bancoDeDadosDao.obter(it))
					}
				}
				
								
			} else {
				scriptDao.removerExecucoesDoScript(scriptOriginal)
			}
			
			scriptDao.salvar(scriptOriginal)
		} else {
			if (bancoDeDadosKey != null) {
				bancoDeDadosKey.each() {
					script.adicionarExecucaoParaOBanco(bancoDeDadosDao.obter(it))
				}
			}
			scriptDao.salvar(script)

			if (ticketKey != null) {
				Ticket ticket = ticketDao.obter(ticketKey)
				ticket.setScript(script)
				ticketDao.salvar(ticket)
			}
		}
		
		"redirect:/scripts"
	}
	
	@RequestMapping(value = "/{scriptKey}", method = DELETE)
	String excluir( Model model, @PathVariable  int scriptKey) {
		Script script = scriptDao.obter(scriptKey)
		Ticket ticket = script.getTicket()
		if (ticket != null) {
			ticket.setScript(null)
			ticketDao.salvar(ticket)
		}
		scriptDao.excluir(script)
		"redirect:/scripts"
	}
	
	@RequestMapping(value="/{scriptKey}/json", method=GET)
	@ResponseBody String verScript( HttpServletResponse response, @PathVariable  int scriptKey) {
		Script script = scriptDao.obter(scriptKey)
		String.format("{descricao: '%s', script: '%s'}", script.descricao, JavaScriptUtils.javaScriptEscape(script.script))
	}
		
}
