package br.com.bluesoft.pronto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BurndownController {

	@RequestMapping("/burndown.action")
	public String burndown() {
		return "/burndown/burndown.burndown.jsp";
	}
	
}
