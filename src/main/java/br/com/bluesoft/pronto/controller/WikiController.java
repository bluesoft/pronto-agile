package br.com.bluesoft.pronto.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.service.WikiFormatter;

@Controller
public class WikiController {


	@RequestMapping("/wiki/parse.action")
	public String parse(HttpServletResponse response, String data) throws Exception {
		response.getOutputStream().print(WikiFormatter.toHtml(data));
		return null;
	}
}
