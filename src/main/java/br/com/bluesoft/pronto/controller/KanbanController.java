package br.com.bluesoft.pronto.controller;

import org.springframework.stereotype.Controller;

@Controller
public class KanbanController {

	private static final String VIEW_KANBAN = "kaban.index.jsp";

	public String index() {

		return VIEW_KANBAN;
	}

}
