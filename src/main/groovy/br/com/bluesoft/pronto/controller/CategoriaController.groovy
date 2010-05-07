package br.com.bluesoft.pronto.controller;

import org.springframework.ui.Model;
import br.com.bluesoft.pronto.dao.CategoriaDao;
import br.com.bluesoft.pronto.core.Cor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import br.com.bluesoft.pronto.core.Papel;
import org.springframework.ui.Model;
import br.com.bluesoft.pronto.service.Seguranca;
import br.com.bluesoft.pronto.model.Categoria;
import org.springframework.web.bind.annotation.PathVariable;
import br.com.bluesoft.pronto.dao.CategoriaDao;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller 
@RequestMapping("/categorias")
class CategoriaController {
	
	@Autowired private CategoriaDao categoriaDao
	
	@RequestMapping(value='/{categoriaKey}', method=DELETE)
	String excluir(Model model, @PathVariable int categoriaKey) {
		Seguranca.validarPermissao Papel.PRODUCT_OWNER
		try {
			categoriaDao.excluir(new Categoria(categoriaKey: categoriaKey))
			"redirect:/categorias?mensagem=Categoria excluída com sucesso."
		} catch (e) {
			"redirect:/categorias?erro=Esta categoria está vinculada há alguns tickets e não pode ser excluída."
		}
	}
	
	@RequestMapping(method=GET)
	String index(Model model) {
		model.addAttribute 'categorias', categoriaDao.listar()
		"/categorias/categorias.listar.jsp"
	}
	
	@RequestMapping(value='/novo', method=GET)
	String novo(Model model) {
		Seguranca.validarPermissao Papel.PRODUCT_OWNER
		model.addAttribute 'categoria', new Categoria()
		model.addAttribute 'cores', Cor.values()
		"/categorias/categorias.editar.jsp"
	}
	
	@RequestMapping(value='/{categoriaKey}', method=GET)
	String editar(Model model, @PathVariable int categoriaKey) {
		Seguranca.validarPermissao Papel.PRODUCT_OWNER
		model.addAttribute 'categoria', categoriaDao.obter(categoriaKey)
		model.addAttribute 'cores', Cor.values()
		"/categorias/categorias.editar.jsp"
	}
	
	@RequestMapping(method=POST)
	String salvar(Model model, Categoria categoria) {
		Seguranca.validarPermissao Papel.PRODUCT_OWNER
		def tx = categoriaDao.getSession().beginTransaction()
		categoriaDao.salvar categoria
		tx.commit()
		"redirect:/categorias?mensagem=Categoria salva com sucesso."
	}
	
}
