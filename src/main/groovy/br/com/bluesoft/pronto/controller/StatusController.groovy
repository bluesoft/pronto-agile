package br.com.bluesoft.pronto.controller

import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.service.PerformanceCounterService;
import br.com.bluesoft.pronto.to.ApplicationStatusTO;

@Controller
@RequestMapping("/openServices/agent/*")
class StatusController {
	
	@Autowired PerformanceCounterService performanceCounterService
	
	@RequestMapping
	void status(HttpServletRequest request, HttpServletResponse response) {
		def callback = request.getParameter("callback");
		def status;
		try {
			status = performanceCounterService.criaStatus();
		} catch (final Exception e) {
			throw new ServletException(e);
		}

		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache");

		if (callback != null) { // jsonp
			final PrintWriter out = response.getWriter();
			out.print(callback + "(");
			out.print(status.toJSON());
			out.print(");");
		} else {
			response.setContentType("application/json");
			response.getWriter().print(status.toJSON());
		}
	}
}
