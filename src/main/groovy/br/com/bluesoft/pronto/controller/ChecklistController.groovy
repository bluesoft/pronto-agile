package br.com.bluesoft.pronto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.bluesoft.pronto.core.Papel;
import br.com.bluesoft.pronto.dao.ChecklistDao;
import br.com.bluesoft.pronto.model.Checklist;
import br.com.bluesoft.pronto.service.ChecklistService;
import br.com.bluesoft.pronto.service.Seguranca;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller 
@RequestMapping("/checklists")
class ChecklistController {
	
	@Autowired private ChecklistService checklistService
	
	@RequestMapping(value='/{checklistKey}', method=DELETE)
	String excluir(Model model, @PathVariable int checklistKey) {
		Seguranca.validarPermissao Papel.EQUIPE
		checklistService.excluir(checklistKey)
		"redirect:/checklists?message=Excluído com Sucesso"
	}
	
	@RequestMapping(method=GET)
	String index(Model model) {
		model.addAttribute 'checklists', checklistService.listarModelos()
		"/checklists/checklists.listar.jsp"
	}
	
	@RequestMapping(value='/novo', method=GET)
	String novo(Model model) {
		Seguranca.validarPermissao Papel.EQUIPE
		model.addAttribute 'checklist', new Checklist()
		"/checklists/checklists.editar.jsp"
	}
	
	@RequestMapping(value='/{checklistKey}', method=GET)
	String editar(Model model, @PathVariable int checklistKey) {
		Seguranca.validarPermissao Papel.EQUIPE
		model.addAttribute 'checklist', checklistService.obter(checklistKey)
		"/checklists/checklists.editar.jsp"
	}
	
	@RequestMapping(method=POST)
	String salvar(Model model, Checklist checklist) {
		Seguranca.validarPermissao Papel.EQUIPE
		boolean novo = (checklist.checklistKey == null || checklist.checklistKey == 0) 
		checklistService.salvar checklist
		return (!novo ? "redirect:/checklists" : "redirect:/checklists/${checklist.checklistKey}") + '?mensagem=Salvo com Sucesso'
	}
	
	@ResponseBody
	@RequestMapping(value="/removerItem")
	def removerChecklistItem(int checklistItemKey) {
		return checklistService.removerItem(checklistItemKey)
	}
	
	@ResponseBody
	@RequestMapping(value="/modelos", method=GET)
	def modelos() {
		def modelos = checklistService.listarModelos().collect {
			[nome:it.nome, checklistKey:it.checklistKey]
		}
		return modelos 
	}
	
	@ResponseBody
	@RequestMapping(value="/{checklistKey}/incluirItem", method=POST)
	def incluirItemNoChecklist(@PathVariable int checklistKey, String descricao) {
		checklistService.incluirItem(checklistKey, descricao).checklistItemKey
	}
	
}
