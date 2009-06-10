package br.com.bluesoft.pronto.controller;

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
			return LoginController.VIEW_BEM_VINDO;
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
			burnNumber = burnNumber - esforco;
			burnValues.add(burnNumber);
		}

		final JSONObject raiz = new JSONObject();

		final JSONObject title = new JSONObject();
		title.set("text", "Sprint " + sprint.getNome());
		raiz.put("title", title);

		// JSONObject y_legend = new JSONObject();
		// y_legend.set("text", "Y Legend");
		// raiz.put("y_legend", y_legend);

		// final JSONObject x_legend = new JSONObject();
		// x_legend.set("text", "Dias");
		// raiz.put("x_legend", x_legend);

		final JSONArray elements = new JSONArray();

		final JSONObject element1 = new JSONObject();
		element1.set("type", "line");
		element1.set("text", "Burndown");
		element1.set("values", new JSONArray(burnValues));
		elements.put(element1);

		// JSONObject element2 = new JSONObject();
		// element2.set("type", "bar");
		// element2.set("text", "Nao sei");
		// element2.set("values", new JSONArray(new int[]{9,5,4,3,6,7,8,8,9}));
		// elements.put(element2);

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
