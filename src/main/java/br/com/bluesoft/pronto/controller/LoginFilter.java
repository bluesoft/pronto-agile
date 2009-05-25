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

public class LoginFilter implements Filter {

	private static final String START_URI = "/start.action";
	private static final String LOGIN_URI = "/login.action";

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		boolean logado = request.getSession().getAttribute("usuario") != null;
		boolean isStartAction = request.getRequestURI().contains(START_URI);
		boolean isLoginAction = request.getRequestURI().contains(LOGIN_URI);
		boolean isAProtectedResource = request.getRequestURI().contains("jsp") || request.getRequestURI().contains("action");
		
		if (!logado && !isLoginAction && !isStartAction && isAProtectedResource ) {
			HttpServletResponse response = (HttpServletResponse) servletResponse;
			response.sendRedirect(request.getContextPath() + START_URI);
		} else {
			chain.doFilter(servletRequest, servletResponse);
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
