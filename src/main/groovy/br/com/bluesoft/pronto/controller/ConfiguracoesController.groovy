package br.com.bluesoft.pronto.controller

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping

import br.com.bluesoft.pronto.dao.ConfiguracaoDao;
import static org.springframework.web.bind.annotation.RequestMethod.*

@RequestMapping("/configuracoes")
class ConfiguracoesController {
	
	@Autowired ConfiguracaoDao configuracaoDao
	
	@RequestMapping(method=GET)
	String exibir(Model model) {
		model.addAttribute('configuracoes', configuracaoDao.listar())
		"/configuracoes/configuracoes.exibir.jsp";
	}
	
	@RequestMapping(method=POST)
	String salvar(Model model, Map configuracoes) {
		model.addAttribute 'mensagem', 'Configurações Salvas com Sucesso!'
		"/configuracoes/configuracoes.exibir.jsp";
	}
	
}
