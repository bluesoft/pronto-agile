package br.com.bluesoft.pronto.controller;

import org.springframework.ui.Model;
import br.com.bluesoft.pronto.dao.MotivoReprovacaoDao;
import br.com.bluesoft.pronto.core.Cor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import br.com.bluesoft.pronto.core.Papel;
import org.springframework.ui.Model;
import br.com.bluesoft.pronto.service.Seguranca;
import br.com.bluesoft.pronto.model.MotivoReprovacao;
import org.springframework.web.bind.annotation.PathVariable;
import br.com.bluesoft.pronto.dao.MotivoReprovacaoDao;
import br.com.bluesoft.pronto.dao.MotivoReprovacaoDao;

import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller 
@RequestMapping("/motivosReprovacao")
class MotivoReprovacaoController {
	
	@Autowired private MotivoReprovacaoDao motivoReprovacaoDao
	
	@RequestMapping(value='/{motivoReprovacaoKey}', method=DELETE)
	String excluir(Model model, @PathVariable int motivoReprovacaoKey) {
		Seguranca.validarPermissao Papel.EQUIPE
		try {
			motivoReprovacaoDao.excluir(new MotivoReprovacao(motivoReprovacaoKey: motivoReprovacaoKey))
			"redirect:/motivosReprovacao?message=Excluído com Sucesso"
		} catch (e) {
			"redirect:/motivosReprovacao?erro=Esta Motivo de Reprovação está Vinculado há Alguns Tickets e Não pode ser Excluído"
		}
	}
	
	@RequestMapping(method=GET)
	String index(Model model) {
		model.addAttribute 'motivosReprovacao', motivoReprovacaoDao.listar()
		"/motivosReprovacao/motivosReprovacao.listar.jsp"
	}
	
	@RequestMapping(value='/novo', method=GET)
	String novo(Model model) {
		Seguranca.validarPermissao Papel.EQUIPE
		model.addAttribute 'motivoReprovacao', new MotivoReprovacao()
		"/motivosReprovacao/motivosReprovacao.editar.jsp"
	}
	
	@RequestMapping(value='/{motivoReprovacaoKey}', method=GET)
	String editar(Model model, @PathVariable int motivoReprovacaoKey) {
		Seguranca.validarPermissao Papel.EQUIPE
		model.addAttribute 'motivoReprovacao', motivoReprovacaoDao.obter(motivoReprovacaoKey)
		"/motivosReprovacao/motivosReprovacao.editar.jsp"
	}
	
	@RequestMapping(method=POST)
	String salvar(Model model, MotivoReprovacao motivoReprovacao) {
		Seguranca.validarPermissao Papel.EQUIPE
		def tx = motivoReprovacaoDao.getSession().beginTransaction()
		motivoReprovacaoDao.salvar motivoReprovacao
		tx.commit()
		"redirect:/motivosReprovacao?message=Salvo com Sucesso"
	}
	
}
