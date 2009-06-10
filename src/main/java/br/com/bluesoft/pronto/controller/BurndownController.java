package br.com.bluesoft.pronto.controller;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.dao.SprintDao;
import br.com.bluesoft.pronto.model.Sprint;

@Controller
public class BurndownController {

	@Autowired
	private SprintDao sprintDao;

	@RequestMapping("/burndown/burndown.action")
	public String burndown(final Model model, final Integer sprintKey) {
		model.addAttribute("sprintKey", sprintKey);

		if (sprintKey == null && sprintDao.getSprintAtual() == null) {
			model.addAttribute("erro", "Não há um Sprint Atual!");
			return "/branca.jsp";
		}

		return "/burndown/burndown.burndown.jsp";
	}

	@RequestMapping("/burndown/data.action")
	public String data(final HttpServletResponse response, final Integer sprintKey) throws Exception {

		final Sprint sprint;
		if (sprintKey == null) {
			sprint = sprintDao.getSprintAtual();
		} else {
			sprint = sprintDao.obter(sprintKey);
		}

		final double esforcoTotal = sprint.getEsforcoTotal();
		final Map<String, Double> mapaEsforcoPorDia = sprint.getMapaEsforcoPorDia();

		double burnNumber = esforcoTotal;
		final List<Double> burnValues = new LinkedList<Double>();
		burnValues.add(burnNumber);
		for (final Double esforco : mapaEsforcoPorDia.values()) {
			burnNumber = new BigDecimal(burnNumber - esforco).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			burnValues.add(burnNumber);
		}

		burnNumber = esforcoTotal;
		final double media = esforcoTotal / mapaEsforcoPorDia.values().size();
		final List<Double> idealValues = new LinkedList<Double>();
		idealValues.add(burnNumber);
		for (int i = 0; i < mapaEsforcoPorDia.values().size(); i++) {
			burnNumber = new BigDecimal(burnNumber - media).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			idealValues.add(burnNumber);
		}

		final JSONObject raiz = new JSONObject();

		final JSONObject title = new JSONObject();
		title.set("text", "Sprint " + sprint.getNome());
		raiz.put("title", title);

		final JSONArray elements = new JSONArray();

		final JSONObject element1 = new JSONObject();
		element1.set("type", "area");
		element1.set("width", "2");
		element1.set("colour", "#FF0000");

		element1.set("fill", "#E01B49");
		element1.set("fill-alpha", "0.4");

		final JSONObject dotStyle = new JSONObject();
		dotStyle.set("type", "hollow-dot");
		element1.set("dotStyle", dotStyle);

		element1.set("text", "Ideal");
		element1.set("values", new JSONArray(idealValues));
		elements.put(element1);

		final JSONObject element2 = new JSONObject();
		element2.set("type", "area");
		element2.set("width", "2");
		element2.set("colour", "#00666cc");

		element2.set("fill", "#0066cc");
		element2.set("fill-alpha", "0.4");

		element2.set("text", "Realizado");
		element2.set("values", new JSONArray(burnValues));
		elements.put(element2);

		raiz.put("elements", elements);

		final JSONObject x_axis = new JSONObject();
		final JSONArray xLabelArray = new JSONArray();
		xLabelArray.put("início");
		for (final String data : mapaEsforcoPorDia.keySet()) {
			xLabelArray.put(data);
		}

		final JSONObject x_labels = new JSONObject();
		x_labels.put("labels", xLabelArray);
		if (xLabelArray.length() > 7) {
			x_labels.put("rotate", 270);
		}

		x_axis.set("stroke", 1);

		x_axis.set("labels", x_labels);
		x_axis.set("tick_height", 10);
		raiz.put("x_axis", x_axis);

		final JSONObject y_axis = new JSONObject();
		y_axis.set("stroke", 1);
		y_axis.set("steps", esforcoTotal / 8);
		y_axis.set("max", esforcoTotal);
		y_axis.set("offset", 2);
		y_axis.set("tick_length", 5);
		raiz.put("y_axis", y_axis);

		raiz.put("bg_colour", "#FFFFFF");

		response.getOutputStream().print(raiz.toString());
		response.getOutputStream().flush();

		System.out.println(raiz.toString());
		return null;
	}
}
