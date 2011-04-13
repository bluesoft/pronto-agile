package br.com.bluesoft.pronto.controller;

import org.springframework.ui.Model;
import br.com.bluesoft.pronto.dao.ProjetoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import br.com.bluesoft.pronto.core.Papel;
import org.springframework.ui.Model;
import br.com.bluesoft.pronto.service.Seguranca;
import br.com.bluesoft.pronto.model.Projeto;
import org.springframework.web.bind.annotation.PathVariable;
import br.com.bluesoft.pronto.dao.ProjetoDao;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller 
@RequestMapping("/projetos")
class ProjetoController {
	
	@Autowired private ProjetoDao projetoDao
	
	@RequestMapping(value='/{projetoKey}', method=DELETE)
	String excluir(Model model, @PathVariable int projetoKey) {
		Seguranca.validarPermissao Papel.PRODUCT_OWNER
		try {
			projetoDao.excluir(new Projeto(projetoKey: projetoKey))
			"redirect:/projetos?mensagem=Projeto excluído com sucesso."
		} catch (e) {
			"redirect:/projetos?erro=Este projeto está vinculado há alguns tickets e não pode ser excluída."
		}
	}
	
	@RequestMapping(method=GET)
	String index(Model model) {
		model.addAttribute 'projetos', projetoDao.listar()
		"/projetos/projetos.listar.jsp"
	}
	
	@RequestMapping(value='/novo', method=GET)
	String novo(Model model) {
		Seguranca.validarPermissao Papel.PRODUCT_OWNER
		model.addAttribute 'projeto', new Projeto()
		"/projetos/projetos.editar.jsp"
	}
	
	@RequestMapping(value='/{projetoKey}', method=GET)
	String editar(Model model, @PathVariable int projetoKey) {
		Seguranca.validarPermissao Papel.PRODUCT_OWNER
		model.addAttribute 'projeto', projetoDao.obter(projetoKey)
		"/projetos/projetos.editar.jsp"
	}
	
	@RequestMapping(method=POST)
	String salvar(Model model, Projeto projeto) {
		Seguranca.validarPermissao Papel.PRODUCT_OWNER
		def tx = projetoDao.getSession().beginTransaction()
		projetoDao.salvar projeto
		tx.commit()
		"redirect:/projetos?mensagem=Projeto salvo com sucesso."
	}
	
}
