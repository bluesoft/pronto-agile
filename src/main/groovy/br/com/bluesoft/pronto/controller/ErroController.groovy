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

package br.com.bluesoft.pronto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.SegurancaException;

@Controller
public class ErroController {

	@RequestMapping("/erro")
	public String erro() {
		return "redirect:/branca.jsp?erro=Ocorreu um Erro!";
	}

	@RequestMapping("/acessoNegado")
	public String acessoNegado() {
		return "redirect:/branca.jsp?erro=Acesso Negado!";
	}
	
	@ExceptionHandler(SegurancaException.class)
	public String seguracaException(SegurancaException se){
		return acessoNegado()
	}
	
	@ExceptionHandler(Exception.class)
	public String erro(Exception se){
		return "redirect:/branca.jsp?erro=${se.message}";
	}

}
