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

package br.com.bluesoft.pronto.controller

import javax.servlet.http.HttpSession

import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

import br.com.bluesoft.pronto.model.Usuario
import br.com.bluesoft.pronto.service.Seguranca
import static org.springframework.web.bind.annotation.RequestMethod.*

@Controller
class LoginController {
	
	public static final String ACTION_INICIAL = "/dashboard"
	public static final String ACTION_BACKLOG_DO_CLIENTE = "/clientes/backlog"
	public static final String VIEW_BEM_VINDO = "/bemVindo.jsp"
	
	@Autowired SessionFactory sessionFactory
	
	@RequestMapping(value = "/login", method = GET)
	String start(final HttpSession httpSession) {
		final Usuario usuarioLogado = (Usuario) httpSession.getAttribute("usuarioLogado")
		if (usuarioLogado != null) {
			return pageToStart(httpSession)
		} else {
			return "/login/login.login.jsp"
		}
	}
	private pageToStart(HttpSession httpSession) {
		final Usuario usuarioLogado = (Usuario) httpSession.getAttribute("usuarioLogado")
		if (usuarioLogado.isClientePapel()) {
			return "redirect:" + ACTION_BACKLOG_DO_CLIENTE
		} else {
			if (httpSession.getAttribute('lastRequestURI') != null) {
				def uri = httpSession.getAttribute("lastRequestURI")
				httpSession.removeAttribute("lastRequestURI")
				return "redirect:${uri}"
			} else {
				return "redirect:" + ACTION_INICIAL
			}
		}
	}
	
	@RequestMapping(value = "/logar", method=POST)
	String login(final Model model, final HttpSession httpSession, final String username, final String password) {
		final String md5 = Seguranca.encrypt(password)
		final Usuario usuario = (Usuario) sessionFactory.getCurrentSession().createQuery("select distinct u from Usuario u inner join fetch u.papeis where u.username = :username and u.password = :password").setString("username", username).setString("password", md5).uniqueResult()
		if (usuario == null) {
			model.addAttribute("erro", "Usuário e/ou senha inválidos!")
			return "redirect:/login"
		} else {
			httpSession.setAttribute("usuarioLogado", usuario)
			return pageToStart(httpSession)
		}
		
	}
	
	@RequestMapping("/logout")
	String logout(final Model model, final HttpSession httpSession) {
		httpSession.removeAttribute("usuarioLogado")
		return "/login/login.login.jsp"
	}
	
}
