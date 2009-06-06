package br.com.bluesoft.pronto.controller;

import java.io.IOException;

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

public class LoginFilter implements Filter {

	private static final String START_URI = "/start.action";
	private static final String LOGIN_URI = "/login.action";

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain chain) throws IOException, ServletException {

		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		final Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("usuarioLogado");
		final boolean logado = usuarioLogado != null;
		final boolean isStartAction = request.getRequestURI().contains(START_URI);
		final boolean isLoginAction = request.getRequestURI().contains(LOGIN_URI);
		final boolean isAProtectedResource = request.getRequestURI().contains("jsp") || request.getRequestURI().contains("action");

		if (!logado && !isLoginAction && !isStartAction && isAProtectedResource) {
			final HttpServletResponse response = (HttpServletResponse) servletResponse;
			response.sendRedirect(request.getContextPath() + START_URI);
		} else {
			Seguranca.setUsuario(usuarioLogado);
			chain.doFilter(servletRequest, servletResponse);
			Seguranca.removeUsuario();
		}

	}

}
