package br.com.bluesoft.pronto.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BurndownController {

	@RequestMapping("/burndown/burndown.action")
	public String burndown() {
		return "/burndown/burndown.burndown.jsp?ofc=data.action";
	}
	
	@RequestMapping("/burndown/data.action")
	public String data(HttpServletResponse response) throws Exception {
		
		
		JSONObject raiz = new JSONObject();
		
		JSONObject title = new JSONObject();
		title.set("text", "Burndown Chart");
		raiz.put("title", title);
		
		JSONObject y_legend = new JSONObject();
		y_legend.set("text", "Y Legend");
		raiz.put("y_legend", y_legend);
		
		JSONArray elements = new JSONArray();
		
		JSONObject element1 = new JSONObject();
		element1.set("type", "bar");
		element1.set("text", "Page Views");
		element1.set("values", new JSONArray(new int[]{6,7,9,5,7,6,9,7,3}));
		elements.put(element1);
		
		raiz.put("elements", elements);
		
	
		JSONObject x_axis = new JSONObject();
		x_axis.set("stroke", 1);
		x_axis.set("labels", new JSONArray(new String[]{"January","February","March","April","May","June","July","August","Spetember"}));
		raiz.put("y_legend", x_axis);
		
		
		JSONObject y_axis = new JSONObject();
		y_axis.set("stroke", 4);
		y_axis.set("max", 20);
		
		raiz.put("y_legend", y_axis);
		
		response.getOutputStream().print(raiz.toString());

		
		System.out.println(raiz.toString());
		return null;
	}
	
}


/*
 
 
  "x_axis":{
    "stroke":1,
    "tick_height":10,
    "colour":"#d000d0",
    "grid_colour":"#00ff00",
    "labels": {
        "labels": ["January","February","March","April","May","June","July","August","Spetember"]
    }
   },
 
  "y_axis":{
    "stroke":      4,
    "tick_length": 3,
    "colour":      "#d000d0",
    "grid_colour": "#00ff00",
    "offset":      0,
    "max":         20
  }
}
 
 */