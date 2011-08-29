package br.com.bluesoft.pronto.controller;

import org.springframework.ui.Model;
import br.com.bluesoft.pronto.dao.ProjetoDao;
import static org.springframework.web.bind.annotation.RequestMethod.*

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

import br.com.bluesoft.pronto.core.KanbanStatus
import br.com.bluesoft.pronto.core.Papel
import br.com.bluesoft.pronto.dao.KanbanStatusDao
import br.com.bluesoft.pronto.dao.MilestoneDao
import br.com.bluesoft.pronto.dao.ProjetoDao
import br.com.bluesoft.pronto.model.Milestone
import br.com.bluesoft.pronto.model.Projeto
import br.com.bluesoft.pronto.service.Seguranca

@Controller
@RequestMapping("/projetos")
class ProjetoController {

	@Autowired private ProjetoDao projetoDao
	@Autowired private KanbanStatusDao kanbanStatusDao
	@Autowired private MilestoneDao milestoneDao

	@RequestMapping(value='/{projetoKey}', method=DELETE)
	String excluir(Model model, @PathVariable int projetoKey) {
		Seguranca.validarPermissao Papel.PRODUCT_OWNER
		try {
			projetoDao.excluir(projetoDao.obter(projetoKey))
			"redirect:/projetos?mensagem=Projeto excluído com sucesso."
		} catch (e) {
			"redirect:/projetos?erro=Este projeto está vinculado há alguns tickets e/ou sprints e não pode ser excluído."			
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

	@RequestMapping(value="/", method=POST)
	String salvar(Model model, Projeto projeto) {
		Seguranca.validarPermissao Papel.PRODUCT_OWNER
		
		boolean novo = projeto.projetoKey <= 0
		
		def tx = projetoDao.getSession().beginTransaction()
		projetoDao.salvar projeto
		
		if(novo) {
			KanbanStatus kanbanStatus = new KanbanStatus()
			kanbanStatus.descricao = "To Do"
			kanbanStatus.projeto = projeto
			kanbanStatus.ordem = 1
			kanbanStatus.inicio = true
			kanbanStatusDao.salvar kanbanStatus
			
			kanbanStatus = new KanbanStatus()
			kanbanStatus.descricao = 'Doing'
			kanbanStatus.projeto = projeto
			kanbanStatus.ordem = 2
			kanbanStatusDao.salvar kanbanStatus
			
			kanbanStatus = new KanbanStatus()
			kanbanStatus.descricao = 'Done'
			kanbanStatus.projeto = projeto
			kanbanStatus.ordem = 99999
			kanbanStatus.fim = true
			kanbanStatusDao.salvar kanbanStatus
		}
		
		tx.commit()
		
		if (novo) {
			return "redirect:/projetos/${projeto.projetoKey}?mensagem=Projeto salvo com sucesso."
		} else {
			return  "redirect:/projetos?mensagem=Projeto salvo com sucesso."
		}
	}

	@RequestMapping(method=POST)
	@ResponseBody
	String incluirEtapa(int projetoKey, String nome){
		KanbanStatus kanbanStatus = new KanbanStatus()
		kanbanStatus.descricao = nome
		kanbanStatus.projeto = projetoDao.proxy(projetoKey)
		kanbanStatus.ordem = 99
		kanbanStatusDao.salvar(kanbanStatus)
		return kanbanStatus.kanbanStatusKey
	}
	
	@RequestMapping(method=POST)
	@ResponseBody
	String excluirEtapa(int kanbanStatusKey){
		kanbanStatusDao.excluir(kanbanStatusDao.obter(kanbanStatusKey))
		return "true"
	}
	
	@RequestMapping(method=POST)
	@ResponseBody
	String editarEtapa(int kanbanStatusKey, String nome){
		def etapa = kanbanStatusDao.obter(kanbanStatusKey)
		etapa.descricao = nome
		kanbanStatusDao.salvar etapa
		return "true"
	}
	
	@RequestMapping(method=POST)
	@ResponseBody
	String atualizarOrdensDasEtapas(Integer[] kanbanStatusKey){
		kanbanStatusKey.eachWithIndex { it, index ->
			def etapa = kanbanStatusDao.obter(it)
			etapa.ordem  = index+10
			kanbanStatusDao.salvar etapa
		}
		return "true"
	}
	
	@RequestMapping(method=POST)
	@ResponseBody
	String incluirMilestone(int projetoKey, String nome){
		Milestone milestone = new Milestone()
		milestone.nome = nome
		milestone.projeto = projetoDao.proxy(projetoKey)
		milestoneDao.salvar(milestone)
		return milestone.milestoneKey
	}
	
	@RequestMapping(method=POST)
	@ResponseBody
	String excluirMilestone(int milestoneKey){
		milestoneDao.excluir(milestoneDao.obter(milestoneKey))
		return "true"
	}
	
	@RequestMapping(method=POST)
	@ResponseBody
	String editarMilestone(int milestoneKey, String nome){
		def milestone = milestoneDao.obter(milestoneKey)
		milestone.nome = nome
		milestoneDao.salvar milestone
		return "true"
	}
	
}
