package br.com.bluesoft.pronto.controller;

import org.springframework.ui.Model;
import br.com.bluesoft.pronto.dao.ModuloDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import br.com.bluesoft.pronto.core.Papel;
import org.springframework.ui.Model;
import br.com.bluesoft.pronto.service.Seguranca;
import br.com.bluesoft.pronto.model.Modulo;
import org.springframework.web.bind.annotation.PathVariable;
import br.com.bluesoft.pronto.dao.ModuloDao;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller 
@RequestMapping("/modulos")
class ModuloController {
	
	@Autowired private ModuloDao moduloDao
	
	@RequestMapping(value='/{moduloKey}', method=DELETE)
	String excluir(Model model, @PathVariable int moduloKey) {
		Seguranca.validarPermissao Papel.PRODUCT_OWNER
		try {
			moduloDao.excluir(new Modulo(moduloKey: moduloKey))
			"redirect:/modulos?mensagem=Módulo excluído com sucesso."
		} catch (e) {
			"redirect:/modulos?erro=Este módulo está vinculado há alguns tickets e não pode ser excluída."
		}
	}
	
	@RequestMapping(method=GET)
	String index(Model model) {
		model.addAttribute 'modulos', moduloDao.listar()
		"/modulos/modulos.listar.jsp"
	}
	
	@RequestMapping(value='/novo', method=GET)
	String novo(Model model) {
		Seguranca.validarPermissao Papel.PRODUCT_OWNER
		model.addAttribute 'modulo', new Modulo()
		"/modulos/modulos.editar.jsp"
	}
	
	@RequestMapping(value='/{moduloKey}', method=GET)
	String editar(Model model, @PathVariable int moduloKey) {
		Seguranca.validarPermissao Papel.PRODUCT_OWNER
		model.addAttribute 'modulo', moduloDao.obter(moduloKey)
		"/modulos/modulos.editar.jsp"
	}
	
	@RequestMapping(method=POST)
	String salvar(Model model, Modulo modulo) {
		Seguranca.validarPermissao Papel.PRODUCT_OWNER
		def tx = moduloDao.getSession().beginTransaction()
		moduloDao.salvar modulo
		tx.commit()
		"redirect:/modulos?mensagem=Módulo salvo com sucesso."
	}
	
}
