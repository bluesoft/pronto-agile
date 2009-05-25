package br.com.bluesoft.pronto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class KanbanController {

	private static final String VIEW_KANBAN = "kanban.kanban.jsp";

	@RequestMapping("kanban/kanban.action")
	public String index() {
		return VIEW_KANBAN;
	}

}
