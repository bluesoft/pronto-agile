package br.com.bluesoft.pronto.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.core.Backlog;
import br.com.bluesoft.pronto.core.KanbanStatus;
import br.com.bluesoft.pronto.core.Papel;
import br.com.bluesoft.pronto.core.TipoDeTicket;
import br.com.bluesoft.pronto.model.Sprint;
import br.com.bluesoft.pronto.model.Ticket;
import br.com.bluesoft.pronto.model.Usuario;
import br.com.bluesoft.pronto.service.Seguranca;

@Controller
public class LoginController {

	private static final String ACTION_KANBAN = "/kanban/kanban.action";

	@Autowired
	private Seguranca seguranca;

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

				usuario.setPassword(seguranca.encrypt("8437"));
				sessionFactory.getCurrentSession().save(usuario);
				sessionFactory.getCurrentSession().flush();

				usuario = new Usuario();
				usuario.setEmail("junior@bluesoft.com.br");
				usuario.setNome("Luiz dos Santos Faias Jr.");
				usuario.setUsername("junior");
				usuario.setPassword(seguranca.encrypt(""));
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

				final Date semanaPassada = new Date(2009 + 1900, 05, 23);
				final Date hoje = new Date();

				Sprint sprint = new Sprint();
				sprint.setNome("Canadá");
				sprint.setDataInicial(semanaPassada);
				sprint.setDataFinal(hoje);
				sessionFactory.getCurrentSession().save(sprint);
				sprint = new Sprint();
				sprint.setNome("Alemanha");
				sprint.setDataInicial(semanaPassada);
				sprint.setDataFinal(hoje);
				sprint.setAtual(true);
				sessionFactory.getCurrentSession().save(sprint);
				sessionFactory.getCurrentSession().flush();

				sessionFactory.getCurrentSession().save(new Backlog(Backlog.IDEIAS, "Idéias"));
				sessionFactory.getCurrentSession().save(new Backlog(Backlog.IMPEDIMENTOS, "Impedimentos"));
				sessionFactory.getCurrentSession().save(new Backlog(Backlog.LIXEIRA, "Lixeira"));
				final Backlog productBacklog = new Backlog(Backlog.PRODUCT_BACKLOG, "Product Backlog");
				sessionFactory.getCurrentSession().save(productBacklog);
				final Backlog sprintBacklog = new Backlog(Backlog.SPRINT_BACKLOG, "Sprint Backlog");
				sessionFactory.getCurrentSession().save(sprintBacklog);
				sessionFactory.getCurrentSession().flush();

				sessionFactory.getCurrentSession().save(new KanbanStatus(KanbanStatus.TO_DO, "To Do"));
				sessionFactory.getCurrentSession().save(new KanbanStatus(KanbanStatus.DOING, "Doing"));
				sessionFactory.getCurrentSession().save(new KanbanStatus(KanbanStatus.TO_TEST, "To Test"));
				sessionFactory.getCurrentSession().save(new KanbanStatus(KanbanStatus.TESTING, "Testing"));
				sessionFactory.getCurrentSession().save(new KanbanStatus(KanbanStatus.DONE, "Done"));

				sessionFactory.getCurrentSession().flush();

				final Ticket cadastro = new Ticket();
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

				final Ticket consulta = new Ticket();
				consulta.setTitulo("Consulta de Cheques");
				consulta.setTipoDeTicket(new TipoDeTicket(2));
				consulta.setSolicitador("Luiz");
				consulta.setCliente("Pedreira");
				consulta.setBacklog(productBacklog);
				consulta.setValorDeNegocio(100);
				consulta.setEsforco(13);
				consulta.setReporter(usuario);
				consulta.setSprint(null);
				sessionFactory.getCurrentSession().save(consulta);

				final Ticket relatorio = new Ticket();
				relatorio.setTitulo("Relatorio de Vendas");
				relatorio.setTipoDeTicket(new TipoDeTicket(2));
				relatorio.setSolicitador("Luiz");
				relatorio.setCliente("Pedreira");
				relatorio.setBacklog(productBacklog);
				relatorio.setValorDeNegocio(100);
				relatorio.setEsforco(13);
				relatorio.setReporter(usuario);
				relatorio.setSprint(null);
				sessionFactory.getCurrentSession().save(relatorio);

				final Ticket cobranca = new Ticket();
				cobranca.setTitulo("Cobrança");
				cobranca.setTipoDeTicket(new TipoDeTicket(2));
				cobranca.setSolicitador("Luiz");
				cobranca.setCliente("Pedreira");
				cobranca.setBacklog(productBacklog);
				cobranca.setValorDeNegocio(100);
				cobranca.setEsforco(13);
				cobranca.setReporter(usuario);
				cobranca.setSprint(null);
				sessionFactory.getCurrentSession().save(cobranca);

				final Ticket bug = new Ticket();
				bug.setTitulo("Bug da Venda Online");
				bug.setTipoDeTicket(new TipoDeTicket(TipoDeTicket.DEFEITO));
				bug.setSolicitador("Luiz");
				bug.setCliente("Pedreira");
				bug.setBacklog(productBacklog);
				bug.setSprint(null);
				bug.setValorDeNegocio(100);
				bug.setEsforco(13);
				bug.setReporter(usuario);
				sessionFactory.getCurrentSession().save(bug);
				sessionFactory.getCurrentSession().flush();

				initialized = true;
			}

		}

		return "/login/login.login.jsp";

	}

	@RequestMapping("/login.action")
	public String login(final Model model, final HttpSession httpSession, final String username, final String password) {
		final String md5 = seguranca.encrypt(password);
		final Usuario usuario = (Usuario) sessionFactory.getCurrentSession().createQuery("from Usuario u where u.username = :username and u.password = :password").setString("username", username).setString("password", md5).uniqueResult();
		if (usuario == null) {
			model.addAttribute("mensagem", "Usuário e/ou senha inválidos!");
			return "/start.action";
		} else {
			httpSession.setAttribute("usuario", usuario);
			return "redirect:" + ACTION_KANBAN;

		}

	}

	@RequestMapping("/logout.action")
	public String login(final Model model, final HttpSession httpSession) {
		httpSession.removeAttribute("usuario");
		return "/login/login.login.jsp";
	}

}
