package br.com.bluesoft.pronto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErroController {

	@RequestMapping("/erro.action")
	public String erro() {
		return "redirect:/branca.jsp?erro=Ocorreu um Erro!";
	}

	@RequestMapping("/acessoNegado.action")
	public String acessoNegado() {
		return "redirect:/branca.jsp?erro=Acesso Negado!";
	}

}
