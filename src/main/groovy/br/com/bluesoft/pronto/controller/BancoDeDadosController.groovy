package br.com.bluesoft.pronto.controller

import static org.springframework.web.bind.annotation.RequestMethod.*

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.bluesoft.pronto.SegurancaException
import br.com.bluesoft.pronto.core.Papel
import br.com.bluesoft.pronto.dao.BancoDeDadosDao
import br.com.bluesoft.pronto.model.BancoDeDados
import br.com.bluesoft.pronto.service.Seguranca


@Controller
@RequestMapping("/bancosDeDados")
class BancoDeDadosController {

	private static final String VIEW_LISTAR = "/bancoDeDados/bancoDeDados.listar.jsp"
	private static final String VIEW_EDITAR = "/bancoDeDados/bancoDeDados.editar.jsp"

	@Autowired private BancoDeDadosDao bancoDeDadosDao

	@RequestMapping(method = RequestMethod.GET)
	String listar(Model model) {
		Seguranca.validarPermissao Papel.EQUIPE
		model.addAttribute("bancos", bancoDeDadosDao.listar())
		VIEW_LISTAR
	}

	@RequestMapping(method = RequestMethod.GET, value="/{bancoDeDadosKey}")
	String editar(Model model, @PathVariable int bancoDeDadosKey) {
		Seguranca.validarPermissao Papel.EQUIPE
		model.addAttribute("bancoDeDados", bancoDeDadosDao.obter(bancoDeDadosKey))
		VIEW_EDITAR
	}

	@RequestMapping(method = RequestMethod.GET, value="/novo")
	String incluir(Model model) throws SegurancaException {
		Seguranca.validarPermissao Papel.EQUIPE
		model.addAttribute "bancoDeDados", new BancoDeDados()
		VIEW_EDITAR
	}

	@RequestMapping(method=[RequestMethod.PUT, RequestMethod.POST], value="/salvar")
	String salvar(Model model, BancoDeDados bancoDeDados)  {
		Seguranca.validarPermissao Papel.EQUIPE
		bancoDeDadosDao.salvar bancoDeDados
		"redirect:/bancosDeDados"
	}

	@RequestMapping(value="/{bancoDeDadosKey}", method = RequestMethod.DELETE)
	String excluir(Model model, @PathVariable int bancoDeDadosKey) {
		Seguranca.validarPermissao Papel.EQUIPE
		bancoDeDadosDao.excluir bancoDeDadosDao.obter(bancoDeDadosKey)
		"redirect:/bancosDeDados?mensagem=Banco de dados excluído com sucesso."
	}

}
