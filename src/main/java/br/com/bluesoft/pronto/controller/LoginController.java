package br.com.bluesoft.pronto.controller;

import javax.servlet.http.HttpSession;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.core.Backlog;
import br.com.bluesoft.pronto.core.Papel;
import br.com.bluesoft.pronto.core.TipoDeTicket;
import br.com.bluesoft.pronto.model.Sprint;
import br.com.bluesoft.pronto.model.Ticket;
import br.com.bluesoft.pronto.model.Usuario;

@Controller
public class LoginController {

	private static final String ACTION_KANBAN = "/kanban/kanban.action";

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

				TipoDeTicket tipoDeTicket = new TipoDeTicket(1, "Idéia");
				sessionFactory.getCurrentSession().save(tipoDeTicket);
				tipoDeTicket = new TipoDeTicket(2, "Estória");
				sessionFactory.getCurrentSession().save(tipoDeTicket);
				tipoDeTicket = new TipoDeTicket(3, "Defeito");
				sessionFactory.getCurrentSession().save(tipoDeTicket);
				tipoDeTicket = new TipoDeTicket(5, "Impedimento");
				sessionFactory.getCurrentSession().save(tipoDeTicket);
				tipoDeTicket = new TipoDeTicket(6, "Tarefa");
				sessionFactory.getCurrentSession().save(tipoDeTicket);
				sessionFactory.getCurrentSession().flush();

				Papel papel = new Papel(Papel.PRODUCT_OWNER, "Product Owner");
				sessionFactory.getCurrentSession().save(papel);
				papel = new Papel(Papel.TESTADOR, "Testador");
				sessionFactory.getCurrentSession().save(papel);
				papel = new Papel(Papel.SCRUM_MASTER, "Scrum Master");
				sessionFactory.getCurrentSession().save(papel);
				papel = new Papel(Papel.SUPORTE, "Suporte");
				sessionFactory.getCurrentSession().save(papel);
				papel = new Papel(Papel.DESENVOLVEDOR, "Desenvolvedor");
				sessionFactory.getCurrentSession().save(papel);
				sessionFactory.getCurrentSession().flush();

				Sprint sprint = new Sprint();
				sprint.setNome("Canadá");
				sessionFactory.getCurrentSession().save(sprint);
				sprint = new Sprint();
				sprint.setNome("Alemanha");
				sprint.setAtual(true);
				sessionFactory.getCurrentSession().save(sprint);
				sessionFactory.getCurrentSession().flush();
				
				sessionFactory.getCurrentSession().save(new Backlog(Backlog.IDEIAS, "Idéias"));
				sessionFactory.getCurrentSession().save(new Backlog(Backlog.IMPEDIMENTOS, "Impedimentos"));
				sessionFactory.getCurrentSession().save(new Backlog(Backlog.LIXEIRA, "Lixeira"));
				Backlog productBacklog = new Backlog(Backlog.PRODUCT_BACKLOG, "Product Backlog");
				sessionFactory.getCurrentSession().save(productBacklog);
				Backlog sprintBacklog = new Backlog(Backlog.SPRINT_BACKLOG, "Sprint Backlog");
				sessionFactory.getCurrentSession().save(sprintBacklog);
				sessionFactory.getCurrentSession().flush();

				Ticket cadastro = new Ticket();
				cadastro.setTitulo("Cadastro de Produtos");
				cadastro.setTipoDeTicket(new TipoDeTicket(2));
				cadastro.setSolicitador("Alberto");
				cadastro.setCliente("Chama");
				cadastro.setBacklog(productBacklog);
				cadastro.setValorDeNegocio(100);
				cadastro.setEsforco(13);
				cadastro.setReporter(usuario);
				cadastro.setSprint(null);
				sessionFactory.getCurrentSession().save(cadastro);
				sessionFactory.getCurrentSession().flush();
				
				Ticket consulta = new Ticket();
				consulta.setTitulo("Consulta de Cheques");
				consulta.setTipoDeTicket(new TipoDeTicket(2));
				consulta.setSolicitador("Luiz");
				consulta.setCliente("Pedreira");
				consulta.setBacklog(sprintBacklog);
				consulta.setSprint(sprint);
				consulta.setValorDeNegocio(100);
				consulta.setEsforco(13);
				consulta.setReporter(usuario);
				sessionFactory.getCurrentSession().save(consulta);
				sessionFactory.getCurrentSession().flush();
				
				initialized = true;
			}

		}

		return "/login/login.login.jsp";

	}

	@RequestMapping("/login.action")
	public String login(final Model model, final HttpSession httpSession, final String username, final String password) {

		final Usuario usuario = (Usuario) sessionFactory.getCurrentSession().createQuery("from Usuario u where u.username = :username and u.password = :password").setString("username", username).setString("password", password).uniqueResult();
		if (usuario == null) {
			model.addAttribute("mensagem", "Usuário e/ou senha inválidos!");
			return "/start.action";
		} else {
			httpSession.setAttribute("usuario", usuario);
			return ACTION_KANBAN;

		}

	}

	@RequestMapping("/logout.action")
	public String login(final Model model, final HttpSession httpSession) {
		httpSession.removeAttribute("usuario");
		return "/login/login.login.jsp";
	}
}
