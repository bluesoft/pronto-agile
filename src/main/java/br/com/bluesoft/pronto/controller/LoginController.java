package br.com.bluesoft.pronto.controller;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.core.TipoDeTicket;
import br.com.bluesoft.pronto.model.Usuario;

@Controller
public class LoginController {

	@Autowired
	private SessionFactory sessionFactory;

	public static Boolean initialized = false;

	@RequestMapping("/start.action")
	public String start() {

		synchronized (initialized) {

			if (!initialized) {
				Usuario usuario = new Usuario();
				usuario.setEmail("andrefaria@bluesoft.com.br");
				usuario.setNome("André Faria Gomes");
				usuario.setUsername("andrefaria");
				usuario.setPassword("");
				sessionFactory.getCurrentSession().save(usuario);
				sessionFactory.getCurrentSession().flush();

				usuario = new Usuario();
				usuario.setEmail("junior@bluesoft.com.br");
				usuario.setNome("Luiz dos Santos Faias Jr.");
				usuario.setUsername("junior");
				usuario.setPassword("");
				sessionFactory.getCurrentSession().save(usuario);
				sessionFactory.getCurrentSession().flush();

				TipoDeTicket tipoDeTicket = new TipoDeTicket(1, "Estória");
				sessionFactory.getCurrentSession().save(tipoDeTicket);
				tipoDeTicket = new TipoDeTicket(2, "Defeito");
				sessionFactory.getCurrentSession().save(tipoDeTicket);
				tipoDeTicket = new TipoDeTicket(3, "Tarefa");
				sessionFactory.getCurrentSession().save(tipoDeTicket);
				sessionFactory.getCurrentSession().flush();
				initialized = true;
			}

		}

		return "login.login.jsp";

	}

	@RequestMapping("/login.action")
	public String login(Model model, String username, String password) {

		return "login.login.jsp";

	}

}
