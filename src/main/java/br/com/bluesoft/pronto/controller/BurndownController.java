package br.com.bluesoft.pronto.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.model.Sprint;

@Controller
public class BurndownController {

	@Autowired
	private SessionFactory sessionFactory;

	@RequestMapping("/burndown/burndown.action")
	public String burndown() {
		return "/burndown/burndown.burndown.jsp?ofc=data.action";
	}

	@RequestMapping("/burndown/data.action")
	public String data(final HttpServletResponse response) throws Exception {

		final Sprint sprint = (Sprint) sessionFactory.getCurrentSession().createCriteria(Sprint.class).add(Restrictions.eq("atual", true)).uniqueResult();

		final int esforcoTotal = sprint.getEsforcoTotal();

		final List<Date> dias = sprint.getDias();

		final JSONObject raiz = new JSONObject();

		final JSONObject title = new JSONObject();
		title.set("text", "Sprint " + sprint.getNome());
		raiz.put("title", title);
		// JSONObject y_legend = new JSONObject();
		// y_legend.set("text", "Y Legend");
		// raiz.put("y_legend", y_legend);

		final JSONArray elements = new JSONArray();

		final JSONObject element1 = new JSONObject();
		element1.set("type", "line");
		element1.set("text", "Burndown");
		element1.set("values", new JSONArray(new int[] { 6, 7, 9, 5, 7, 6, 9, 7, 3 }));
		elements.put(element1);

		// JSONObject element2 = new JSONObject();
		// element2.set("type", "bar");
		// element2.set("text", "Nao sei");
		// element2.set("values", new JSONArray(new int[]{9,5,4,3,6,7,8,8,9}));
		// elements.put(element2);

		raiz.put("elements", elements);

		// JSONObject x_axis = new JSONObject();
		// x_axis.set("stroke", 1);
		// x_axis.set("labels", new JSONArray(new
		// String[]{"January","February","March","April","May","June","July","August","Spetember"}));
		// raiz.put("x_axis", x_axis);

		// JSONObject y_axis = new JSONObject();
		// y_axis.set("stroke", 4);
		// y_axis.set("max", 20);
		// raiz.put("y_axis", y_axis);

		response.getOutputStream().print(raiz.toString());
		response.getOutputStream().flush();

		System.out.println(raiz.toString());
		return null;
	}

}
