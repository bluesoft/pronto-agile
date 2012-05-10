/*
 * Copyright 2009 Pronto Agile Project Management.
 *
 * This file is part of Pronto.
 *
 * Pronto is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Pronto is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Pronto. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package br.com.bluesoft.pronto.controller

import java.util.LinkedList
import java.util.List
import java.util.Map

import javax.servlet.http.HttpServletResponse

import net.sf.json.JSONArray
import net.sf.json.JSONObject

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.bluesoft.pronto.SegurancaException
import br.com.bluesoft.pronto.core.Papel
import br.com.bluesoft.pronto.dao.SprintDao
import br.com.bluesoft.pronto.model.Sprint
import br.com.bluesoft.pronto.service.Seguranca
import br.com.bluesoft.pronto.util.DateUtil;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@RequestMapping("/burndown")
class BurndownController {
	
	@Autowired
	SprintDao sprintDao
	
	@RequestMapping(value='/{sprintKey}', method=RequestMethod.GET)
	String burndownDoSprint(final Model model, @PathVariable int sprintKey, Boolean considerarFimDeSemana) throws SegurancaException {
		return burndown(model, sprintKey, considerarFimDeSemana)
	}
	
	@RequestMapping(method=RequestMethod.GET)
	String burndown(final Model model, final Integer sprintKey, Boolean considerarFimDeSemana) throws SegurancaException {
		
		Seguranca.validarPermissao(Papel.EQUIPE, Papel.PRODUCT_OWNER, Papel.SCRUM_MASTER)

		Sprint sprint = null
		
		if (sprintKey == null) {
			sprint = sprintDao.getSprintAtual();
			if (sprint == null) {
				return LoginController.VIEW_BEM_VINDO
			} else {
				return "redirect:/burndown/${sprint.sprintKey}"
			}
		} else {
			sprint = sprintDao.obter(sprintKey)
		}

		model.addAttribute "considerarFimDeSemana", considerarFimDeSemana
		if (sprint.getMapaEsforcoPorDia().keySet().size() <= 31) {
			model.addAttribute "sprint", sprint
			model.addAttribute "sprints", sprintDao.listarSprintsEmAberto()
			return "/burndown/burndown.burndown.jsp"			
		} else {
			model.addAttribute("erro", 'Não é possível construir um burndown chart de um sprint com mais de 31 dias!')
			return "/branca.jsp";
		}

	}
	
	@RequestMapping(value='/data/{sprintKey}',method=RequestMethod.GET)
	@ResponseBody String data(final HttpServletResponse response, @PathVariable Integer sprintKey, Boolean considerarFimDeSemana) throws Exception {
		
		considerarFimDeSemana = considerarFimDeSemana == null ? false :  considerarFimDeSemana
		
		final Sprint sprint
		if (sprintKey == null) {
			sprint = sprintDao.getSprintAtual()
			sprintKey = sprint.getSprintKey()
		}

		sprint = sprintDao.obterSprintComTicketsETotaisCalculados(sprintKey)
		final double esforcoTotal = sprint.getEsforcoTotal()
		final Map<String, Double> mapaEsforcoPorDia = sprint.getMapaEsforcoPorDia(considerarFimDeSemana)
		final int quantidadeDeDias = mapaEsforcoPorDia.keySet().size()
		if (quantidadeDeDias > 31) {
			return null
		}
		
		double burnNumber = esforcoTotal
		double esforcoRealizado = 0
		final List<Double> burnValues = new LinkedList<Double>()
		burnValues.add(burnNumber)
		for (final Double esforco : mapaEsforcoPorDia.values()) {
			burnNumber = new BigDecimal(burnNumber - esforco).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()
			esforcoRealizado += esforco
			burnValues.add(burnNumber)
		}
		
		burnNumber = esforcoTotal
		final double media = esforcoTotal / mapaEsforcoPorDia.values().size()
		final List<Double> idealValues = new LinkedList<Double>()
		idealValues.add(burnNumber)
		for (int i = 0; i < mapaEsforcoPorDia.values().size(); i++) {
			def atual = burnNumber - media
			burnNumber = new BigDecimal(atual > 0 ? atual : 0).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()
			idealValues.add(burnNumber)
		}
		
		final int diasAteAgora = 0
		final def hoje = new Date()
		mapaEsforcoPorDia.each { k, v -> 
			if (DateUtil.toDate(k) <= hoje) {
				diasAteAgora++
			}
		}
		
		 
		burnNumber = esforcoTotal
		
		final List<Double> previsaoValues = new LinkedList<Double>()
		if (diasAteAgora > 0) {
			final double mediaRealizado = esforcoRealizado / diasAteAgora
			previsaoValues.add(burnNumber)
			for (int i = 0; i < mapaEsforcoPorDia.values().size(); i++) {
				burnNumber = new BigDecimal(burnNumber - mediaRealizado).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()
				previsaoValues.add(burnNumber)
			}
		}
		
		final JSONObject raiz = new JSONObject()
		raiz.put('rotateNames', '1');
		
		final JSONObject title = new JSONObject()
		title.put("text", "Sprint " + sprint.getNome())
		raiz.put("title", title)
		
		final JSONArray elements = new JSONArray()
		
		final JSONObject element1 = new JSONObject()
		element1.put("type", "line")
		element1.put("width", "2")
		element1.put("colour", "#FF0000")
		element1.put("fill", "#E01B49")
		element1.put("fill-alpha", "0.4")
		
		final JSONObject dotStyle = new JSONObject()
		dotStyle.put("type", "hollow-dot")
		element1.put("dotStyle", dotStyle)
		element1.put("text", "Ideal")
		
		def idealValuesArry = new JSONArray()
		idealValuesArry.addAll(idealValues)
		element1.put("values", idealValuesArry)
		
		final JSONObject element2 = new JSONObject()
		element2.put("type", "area")
		element2.put("width", "2")
		element2.put("colour", "#008000")
		element2.put("fill", "#008000")
		element2.put("fill-alpha", "0.4")
		
		element2.put("text", "Realizado")

		def burnValuesArray = new JSONArray()
		burnValuesArray.addAll(burnValues)
		element2.put("values", burnValuesArray)

		elements.add(element1)
		elements.add(element2)
		
		if (diasAteAgora > 0) {
			final JSONObject element3 = new JSONObject()
			element3.put("type", "line")
			element3.put("width", "2")
			element3.put("colour", "#0066cc")
			element3.put("fill", "#0066cc")
			element3.put("fill-alpha", "0.4")
			element3.put("text", "Previsão")
	
			def previsaoValuesArray = new JSONArray()
			previsaoValuesArray.addAll(previsaoValues)
			element3.put("values", previsaoValuesArray)
			
			elements.add(element3)
		}
		
		raiz.put("elements", elements)
		
		final JSONObject x_axis = new JSONObject()
		final JSONArray xLabelArray = new JSONArray()
		xLabelArray.add("início")
		for (final String data : mapaEsforcoPorDia.keySet()) {
			xLabelArray.add(data)
		}
		
		final JSONObject x_labels = new JSONObject()
		x_labels.put("labels", xLabelArray)
		if (xLabelArray.size() > 7) {
			x_labels.put("rotate", 270)
		}
		
		x_axis.put("stroke", 1)
		
		x_axis.put("labels", x_labels)
		x_axis.put("tick_height", 10)
		raiz.put("x_axis", x_axis)
		
		final JSONObject y_axis = new JSONObject()
		y_axis.put("stroke", 1)
		y_axis.put("steps", esforcoTotal / 8)
		y_axis.put("max", esforcoTotal)
		y_axis.put("offset", 2)
		y_axis.put("tick_length", 5)
		raiz.put("y_axis", y_axis)
		
		raiz.put("bg_colour", "#FFFFFF")
		
		return raiz.toString()
	}
	
	@RequestMapping("/flash")
	String flash() {
		"redirect:/burndown/burndown.fullscreen.jsp"
	}
}
