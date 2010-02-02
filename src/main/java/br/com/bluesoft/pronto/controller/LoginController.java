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

import javax.servlet.http.HttpSession;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.model.Usuario;
import br.com.bluesoft.pronto.service.Seguranca;

@Controller
public class LoginController {

	static final String ACTION_KANBAN = "/kanban/kanban.action";
	static final String ACTION_BACKLOG_DO_CLIENTE = "/cliente/backlog.action";
	static final String VIEW_BEM_VINDO = "/bemVindo.jsp";

	@Autowired
	private SessionFactory sessionFactory;

	public static Boolean initialized = false;

	@RequestMapping("/")
	public String root(final HttpSession httpSession) {
		return start(httpSession);
	}

	@RequestMapping("/start.action")
	public String start(final HttpSession httpSession) {
		final Usuario usuarioLogado = (Usuario) httpSession.getAttribute("usuarioLogado");
		if (usuarioLogado != null) {
			if (usuarioLogado.isClientePapel()) {
				return "redirect:" + ACTION_BACKLOG_DO_CLIENTE;
			} else {
				return "redirect:" + ACTION_KANBAN;
			}
		} else {
			return "/login/login.login.jsp";
		}
	}

	@RequestMapping("/login.action")
	public String login(final Model model, final HttpSession httpSession, final String username, final String password) {
		final String md5 = Seguranca.encrypt(password);
		final Usuario usuario = (Usuario) sessionFactory.getCurrentSession().createQuery("select distinct u from Usuario u inner join fetch u.papeis where u.username = :username and u.password = :password").setString("username", username).setString("password", md5).uniqueResult();
		if (usuario == null) {
			model.addAttribute("erro", "Usuário e/ou senha inválidos!");
			return "/start.action";
		} else {
			httpSession.setAttribute("usuarioLogado", usuario);
			if (usuario.isClientePapel()) {
				return "redirect:" + ACTION_BACKLOG_DO_CLIENTE;
			} else {
				return "redirect:" + ACTION_KANBAN;
			}
		}

	}

	@RequestMapping("/logout.action")
	public String login(final Model model, final HttpSession httpSession) {
		httpSession.removeAttribute("usuarioLogado");
		return "/login/login.login.jsp";
	}

}
