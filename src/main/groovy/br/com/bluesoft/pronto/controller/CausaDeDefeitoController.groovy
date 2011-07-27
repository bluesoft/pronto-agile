package br.com.bluesoft.pronto.controller;

import org.springframework.ui.Model;
import br.com.bluesoft.pronto.dao.CausaDeDefeitoDao;
import br.com.bluesoft.pronto.core.Cor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import br.com.bluesoft.pronto.core.Papel;
import org.springframework.ui.Model;
import br.com.bluesoft.pronto.service.Seguranca;
import br.com.bluesoft.pronto.model.CausaDeDefeito;
import org.springframework.web.bind.annotation.PathVariable;
import br.com.bluesoft.pronto.dao.CausaDeDefeitoDao;
import br.com.bluesoft.pronto.dao.CausaDeDefeitoDao;

import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller 
@RequestMapping("/causasDeDefeito")
class CausaDeDefeitoController {
	
	@Autowired private CausaDeDefeitoDao causaDeDefeitoDao
	
	@RequestMapping(value='/{causaDeDefeitoKey}', method=DELETE)
	String excluir(Model model, @PathVariable int causaDeDefeitoKey) {
		Seguranca.validarPermissao Papel.EQUIPE
		try {
			causaDeDefeitoDao.excluir(new CausaDeDefeito(causaDeDefeitoKey: causaDeDefeitoKey))
			"redirect:/causasDeDefeito?message=Excluído com Sucesso"
		} catch (e) {
			"redirect:/causasDeDefeito?erro=Esta Causa está Vinculada há Alguns Tickets e Não pode ser Excluída"
		}
	}
	
	@RequestMapping(method=GET)
	String index(Model model) {
		model.addAttribute 'causasDeDefeito', causaDeDefeitoDao.listar()
		"/causasDeDefeito/causasDeDefeito.listar.jsp"
	}
	
	@RequestMapping(value='/novo', method=GET)
	String novo(Model model) {
		Seguranca.validarPermissao Papel.EQUIPE
		model.addAttribute 'causaDeDefeito', new CausaDeDefeito()
		model.addAttribute 'cores', Cor.values()
		"/causasDeDefeito/causasDeDefeito.editar.jsp"
	}
	
	@RequestMapping(value='/{causaDeDefeitoKey}', method=GET)
	String editar(Model model, @PathVariable int causaDeDefeitoKey) {
		Seguranca.validarPermissao Papel.EQUIPE
		model.addAttribute 'causaDeDefeito', causaDeDefeitoDao.obter(causaDeDefeitoKey)
		model.addAttribute 'cores', Cor.values()
		"/causasDeDefeito/causasDeDefeito.editar.jsp"
	}
	
	@RequestMapping(method=POST)
	String salvar(Model model, CausaDeDefeito causaDeDefeito) {
		Seguranca.validarPermissao Papel.EQUIPE
		def tx = causaDeDefeitoDao.getSession().beginTransaction()
		causaDeDefeitoDao.salvar causaDeDefeito
		tx.commit()
		"redirect:/causasDeDefeito?message=Salvo com Sucesso"
	}
	
}
