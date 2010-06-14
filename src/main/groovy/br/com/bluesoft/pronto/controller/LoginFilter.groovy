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

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.bluesoft.pronto.model.Usuario;
import br.com.bluesoft.pronto.service.Seguranca;
import br.com.bluesoft.pronto.web.servlet.StartupListener;

public class LoginFilter implements Filter {

	private static final String LOGIN_URI = "/login";
	private static final String LOGAR_URI = "/logar";
	private static final String BRANCA_URI = "/branca.jsp";

	private static final Set<String> freeResources;

	static {
		final Set<String> set = new HashSet<String>();
		set.add(".*burndown.*");
		set.add(".*\\.png");
		set.add(".*\\.jpg");
		set.add(".*\\.gif");
		set.add(".*\\.swf");
		set.add(".*\\.css");
		set.add(".*\\.html");
		set.add(".*\\.js");
		freeResources = Collections.unmodifiableSet(set);
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain chain) throws IOException, ServletException {

		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		final HttpServletResponse response = (HttpServletResponse) servletResponse;
		final Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("usuarioLogado");
		final boolean logado = usuarioLogado != null;

		final boolean isAccessDenied = isAccessDenied(request, logado);

		if (isAccessDenied) {
			if (!logado) {
				if (request.getMethod().toUpperCase().equals("GET")) {
					request.getSession().setAttribute 'lastRequestURI', getURIWithoutContextPath(request)
				}
				response.sendRedirect(request.getContextPath() + LOGIN_URI);
				return;
			}
		}

		if (logado) {
			Seguranca.setUsuario(usuarioLogado);
			boolean isRootURL = request.getRequestURI() .equals( request.getContextPath()) || request.getRequestURI() .equals( request.getContextPath() + "/");
			if (isRootURL) {
				response.sendRedirect(request.getContextPath() + "/login");
			} else {
				chain.doFilter(servletRequest, servletResponse);
			}
			Seguranca.removeUsuario();
		} else {
			chain.doFilter(servletRequest, servletResponse);
		}

		
	}
	
	private String getURIWithoutContextPath(HttpServletRequest request) {
		return request.getRequestURI().substring(request.getRequestURI().indexOf('/',1))
	}
	
	private boolean isAccessDenied(final HttpServletRequest request, final boolean logado) {
		final String uri = request.getRequestURI();
		final boolean isLoginAction = uri.equals(request.getContextPath() + LOGIN_URI) || uri.equals(request.getContextPath() + LOGIN_URI + "/");
		final boolean isLogarAction = uri.equals(request.getContextPath() + LOGAR_URI) || uri.equals(request.getContextPath() + LOGAR_URI + "/");
		final boolean isBranca = uri.equals(request.getContextPath() + BRANCA_URI) || uri.equals(request.getContextPath() + BRANCA_URI + "/");

		boolean isntProtected = false;
		for (final String s : freeResources) {
			if (uri.matches(s)) {
				isntProtected = true;
				break;
			}
		}

		final boolean isAccessDenied = !logado && !isLoginAction && !isntProtected && !isLogarAction && !isBranca;
		return isAccessDenied;
	}

}
