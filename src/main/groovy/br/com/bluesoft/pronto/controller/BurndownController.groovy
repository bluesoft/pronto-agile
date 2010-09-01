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
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.bluesoft.pronto.SegurancaException
import br.com.bluesoft.pronto.core.Papel
import br.com.bluesoft.pronto.dao.SprintDao
import br.com.bluesoft.pronto.model.Sprint
import br.com.bluesoft.pronto.service.Seguranca
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@RequestMapping("/burndown")
class BurndownController {
	
	@Autowired
	SprintDao sprintDao
	
	@RequestMapping(value='/{sprintKey}', method=GET)
	String burndownDoSprint(final Model model, @PathVariable int sprintKey) throws SegurancaException {
		return burndown(model, sprintKey)
	}
	
	@RequestMapping(method=GET)
	String burndown(final Model model, final Integer sprintKey) throws SegurancaException {
		
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
		
		if (sprint.getMapaEsforcoPorDia().keySet().size() <= 31) {
			model.addAttribute "sprint", sprint
			model.addAttribute "sprints", sprintDao.listarSprintsEmAberto()
			return "/burndown/burndown.burndown.jsp"			
		} else {
			model.addAttribute("erro", 'Não é possível construir um burndown chart de um sprint com mais de 31 dias!')
			return "/branca.jsp";
		}

	}
	
	@RequestMapping(value='/data/{sprintKey}',method=GET)
	@ResponseBody String data(final HttpServletResponse response, @PathVariable final Integer sprintKey) throws Exception {
		
		final Sprint sprint
		if (sprintKey == null) {
			sprint = sprintDao.getSprintAtualComTickets()
		} else {
			sprint = sprintDao.obterSprintComTicket(sprintKey)
		}
		
		final double esforcoTotal = sprint.getEsforcoTotal()
		final Map<String, Double> mapaEsforcoPorDia = sprint.getMapaEsforcoPorDia()
		final int quantidadeDeDias = mapaEsforcoPorDia.keySet().size()
		if (quantidadeDeDias > 31) {
			return null
		}
		
		double burnNumber = esforcoTotal
		final List<Double> burnValues = new LinkedList<Double>()
		burnValues.add(burnNumber)
		for (final Double esforco : mapaEsforcoPorDia.values()) {
			burnNumber = new BigDecimal(burnNumber - esforco).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()
			burnValues.add(burnNumber)
		}
		
		burnNumber = esforcoTotal
		final double media = esforcoTotal / mapaEsforcoPorDia.values().size()
		final List<Double> idealValues = new LinkedList<Double>()
		idealValues.add(burnNumber)
		for (int i = 0; i < mapaEsforcoPorDia.values().size(); i++) {
			burnNumber = new BigDecimal(burnNumber - media).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()
			idealValues.add(burnNumber)
		}
		
		final JSONObject raiz = new JSONObject()
		
		final JSONObject title = new JSONObject()
		title.put("text", "Sprint " + sprint.getNome())
		raiz.put("title", title)
		
		final JSONArray elements = new JSONArray()
		
		final JSONObject element1 = new JSONObject()
		element1.put("type", "area")
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
		
		elements.add(element1)
		
		final JSONObject element2 = new JSONObject()
		element2.put("type", "area")
		element2.put("width", "2")
		element2.put("colour", "#00666cc")
		
		element2.put("fill", "#0066cc")
		element2.put("fill-alpha", "0.4")
		
		element2.put("text", "Realizado")

		def burnValuesArray = new JSONArray()
		burnValuesArray.addAll(burnValues)
		element2.put("values", burnValuesArray)
		
		elements.add(element2)
		
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
