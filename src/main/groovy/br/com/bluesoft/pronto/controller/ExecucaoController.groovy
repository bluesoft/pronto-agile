package br.com.bluesoft.pronto.controller

import java.util.Date
import java.util.List

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.bluesoft.pronto.SegurancaException
import br.com.bluesoft.pronto.core.Papel
import br.com.bluesoft.pronto.dao.BancoDeDadosDao
import br.com.bluesoft.pronto.dao.ExecucaoDao
import br.com.bluesoft.pronto.dao.KanbanStatusDao;
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
	@Autowired private KanbanStatusDao kanbanStatusDao
	
	@RequestMapping(method = GET)
	String listarTodos(Model model, Integer kanbanStatusKey) {
		listar model, null, false, kanbanStatusKey
	}

	@RequestMapping(value="/pendentes", method = GET)
	String listarPendentes(Model model, Integer kanbanStatusKey) {
		listar model, null, true, kanbanStatusKey
	}

	@RequestMapping(value= '/{bancoDeDadosKey}/pendentes', method = GET)
	String listarPendentesBancoDeDados( Model model,  @PathVariable Integer bancoDeDadosKey, Integer kanbanStatusKey) {
		listar model, bancoDeDadosKey, true, kanbanStatusKey
	}
	
	@RequestMapping(value= '/{bancoDeDadosKey}', method = GET)
	String listarPorBancoDeDados( Model model,  @PathVariable Integer bancoDeDadosKey, Integer kanbanStatusKey) {
		listar model, bancoDeDadosKey, false, kanbanStatusKey
	}
		
	private String listar( Model model,  Integer bancoDeDadosKey,  Boolean pendentes, Integer kanbanStatusKey) {
		
		Seguranca.validarPermissao Papel.EQUIPE
		
		def bancosComExecucoes = []
		
		if (bancoDeDadosKey != null) {
			bancosComExecucoes.add(pendentes ? bancoDeDadosDao.obterComExecucoesPendentes(bancoDeDadosKey,kanbanStatusKey) : bancoDeDadosDao.obterComExecucoes(bancoDeDadosKey,kanbanStatusKey))
		} else {
			bancosComExecucoes.addAll(pendentes ? bancoDeDadosDao.listarComExecucoesPendentes(kanbanStatusKey) : bancoDeDadosDao.listarComExecucoes(kanbanStatusKey))
		}
		
		model.addAttribute "bancosComExecucoes", bancosComExecucoes
		model.addAttribute "bancos", bancoDeDadosDao.listar()
		model.addAttribute "pendentes", pendentes == null ? true : pendentes
		model.addAttribute "bancoDeDadosKey", bancoDeDadosKey
		model.addAttribute "kanbanStatus", kanbanStatusDao.listar()
		model.addAttribute "kanbanStatusKey", kanbanStatusKey
		
		VIEW_LISTAR
	}
	
	
	
	@RequestMapping(value="/gerar", method = POST)
	String gerar( Model model,  Integer bancoDeDadosKey,  Integer[] execucaoKey)  {
		Seguranca.validarPermissao Papel.EQUIPE
		model.addAttribute 'execucaoKey', execucaoKey
		model.addAttribute 'bancoDeDadosKey', bancoDeDadosKey
		VIEW_EXECUCAO
	}
	
	@RequestMapping(value="/gerarScript", method = POST)
	@ResponseBody
	String gerarScript( Model model,  Integer bancoDeDadosKey,  Integer[] execucaoKey)  {
		
		Seguranca.validarPermissao Papel.EQUIPE
		
		def bancoDeDados = bancoDeDadosDao.obter(bancoDeDadosKey)
		
		String script = ''
		script += "/*" + (org.apache.commons.lang.StringUtils.center(" Cliente: " + bancoDeDados.nome + " ", 80, ' ')) + "*/" + "\n"
		
		List<Execucao> execucoes = execucaoDao.listarPorKeys(execucaoKey as List)
		for (Execucao execucao : execucoes) {
			script += ("/*") + (org.apache.commons.lang.StringUtils.center("", 80, '-')) + ("*/") + ("\n")
			script += ("/*") + (org.apache.commons.lang.StringUtils.center(" " + execucao.getScript().getDescricao() + " ", 80, ' ')) + ("*/") + ("\n")
			script += ("/*") + (org.apache.commons.lang.StringUtils.center("", 80, '-')) + ("*/") + ("\n")
			script += (execucao.getScript().getScript()) + ("\n\n\n")
		}
		
		model.addAttribute 'execucaoKey', execucaoKey
		model.addAttribute 'bancoDeDadosKey', bancoDeDadosKey
		return script
	}
	
	@RequestMapping(method = [ POST, PUT ])
	String confirmar( Model model,  Integer bancoDeDadosKey,  Integer[] execucaoKey) {
		
		Seguranca.validarPermissao Papel.EQUIPE
		
		def agora = new Date()
		def execucoes = execucaoDao.listarPorKeys(execucaoKey as List)
		execucoes.each() { Execucao it ->
			it.usuario = Seguranca.usuario
			it.data = agora
			it.script.execucoesPendentes--
			execucaoDao.salvar it
		}
		
		model.addAttribute "execucaoKey", execucaoKey
		
		"redirect:/execucoes/${bancoDeDadosKey}/pendentes"
	}
	
	@RequestMapping(value= '/executar/{execucaoKey}', method = GET)
	String executar( Model model, @PathVariable Integer execucaoKey) {
		
		Seguranca.validarPermissao Papel.EQUIPE
		
		def agora = new Date()
		Execucao execucao = execucaoDao.obter(execucaoKey)

		execucao.usuario = Seguranca.usuario
		execucao.data = agora
		execucao.script.execucoesPendentes--
		execucaoDao.salvar execucao
		
		"redirect:/scripts/${execucao.script.scriptKey}"
	}

	@RequestMapping(value= '/estornarExecucao/{execucaoKey}', method = GET)
	String estornarExecucao( Model model, @PathVariable Integer execucaoKey) {
		
		Seguranca.validarPermissao Papel.EQUIPE
		
		Execucao execucao = execucaoDao.obter(execucaoKey)

		execucao.usuario = null
		execucao.data = null
		execucao.script.execucoesPendentes++
		execucaoDao.salvar execucao
		
		"redirect:/scripts/${execucao.script.scriptKey}"
	}
}
