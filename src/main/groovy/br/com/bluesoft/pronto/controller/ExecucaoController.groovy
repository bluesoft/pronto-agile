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
			bancosComExecucoes.add(pendentes ? bancoDeDadosDao.obterComExecucoesPendentes(bancoDeDadosKey) : bancoDeDadosDao.obterComExecucoes(bancoDeDadosKey))
		} else {
			bancosComExecucoes.addAll(pendentes ? bancoDeDadosDao.listarComExecucoesPendentes() : bancoDeDadosDao.listarComExecucoes())
		}
		
		model.addAttribute "bancosComExecucoes", bancosComExecucoes
		model.addAttribute "bancos", bancoDeDadosDao.listar()
		model.addAttribute "pendentes", pendentes == null ? true : pendentes
		model.addAttribute "bancoDeDadosKey", bancoDeDadosKey
		
		VIEW_LISTAR
	}
	
	
	
	@RequestMapping(value="/gerar", method=POST)
	String gerar( Model model,  Integer bancoDeDadosKey,  Integer[] execucaoKey)  {
		Seguranca.validarPermissao Papel.EQUIPE
		model.addAttribute 'execucaoKey', execucaoKey
		model.addAttribute 'bancoDeDadosKey', bancoDeDadosKey
		VIEW_EXECUCAO
	}
	
	@RequestMapping(value="/gerarScript", method=POST)
	@ResponseBody
	String gerarScript( Model model,  Integer bancoDeDadosKey,  Integer[] execucaoKey)  {
		
		Seguranca.validarPermissao Papel.EQUIPE
		
		String script = ''
		
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
			execucaoDao.salvar it
		}
		
		model.addAttribute "execucaoKey", execucaoKey
		
		"redirect:/execucoes/${bancoDeDadosKey}/pendentes"
	}
	
}
