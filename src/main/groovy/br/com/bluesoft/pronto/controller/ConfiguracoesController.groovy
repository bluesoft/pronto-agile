package br.com.bluesoft.pronto.controller

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping

import br.com.bluesoft.pronto.core.Papel;
import br.com.bluesoft.pronto.dao.ConfiguracaoDao;
import br.com.bluesoft.pronto.model.TipoEstimativa;
import br.com.bluesoft.pronto.service.Seguranca;
import static org.springframework.web.bind.annotation.RequestMethod.*

@Controller
@RequestMapping("/configuracoes")
class ConfiguracoesController {
	
	@Autowired ConfiguracaoDao configuracaoDao
	
	@RequestMapping(method=GET)
	String exibir(Model model) {
		
		Seguranca.validarPermissao Papel.ADMINISTRADOR
		
		model.addAttribute('tiposDeEstimativa', TipoEstimativa.values())
		model.addAttribute('mapa', configuracaoDao.getMapa())
		"/configuracoes/configuracoes.exibir.jsp";
	}
	
	
	
	@RequestMapping(value="/salvar", method=POST)
	String salvar(HttpServletRequest request) {
		
		Seguranca.validarPermissao Papel.ADMINISTRADOR
		
		
		configuracaoDao.atualizarConfiguracoes(request.getParameterMap());
		
		"redirect:/configuracoes?mensagem=Configurações Salvas com Sucesso!";
	}
	
}
