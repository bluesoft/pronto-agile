package br.com.bluesoft.pronto.controller;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.core.Papel;
import br.com.bluesoft.pronto.model.Usuario;
import br.com.bluesoft.pronto.service.Seguranca;

@Controller
public class UsuarioController {

	private static final String VIEW_LISTAR = "usuario.listar.jsp";
	private static final String VIEW_EDITAR = "usuario.editar.jsp";

	@Autowired
	private Seguranca seguranca;

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@RequestMapping("/usuario/listar.action")
	public String listar(final Model model) {
		final List<Usuario> usuarios = sessionFactory.getCurrentSession().createCriteria(Usuario.class).list();
		model.addAttribute("usuarios", usuarios);
		return VIEW_LISTAR;
	}

	@RequestMapping("/usuario/editar.action")
	public String editar(final Model model, final String username) {

		if (username != null) {
			final Usuario usuario = (Usuario) sessionFactory.getCurrentSession().get(Usuario.class, username);
			model.addAttribute("usuario", usuario);
		} else {
			model.addAttribute("usuario", new Usuario());
		}

		model.addAttribute("papeis", sessionFactory.getCurrentSession().createCriteria(Papel.class).list());

		return VIEW_EDITAR;
	}

	@RequestMapping("/usuario/excluir.action")
	public String excluir(final Model model, final String username) {

		int quantidade = sessionFactory.getCurrentSession().createCriteria(Usuario.class).list().size();

		if (quantidade == 1) {
			model.addAttribute("mensagem", "Você não pode excluir todos os usuários do Pronto!.");
			return "forward:listar.action";
		}

		final Usuario usuario = (Usuario) sessionFactory.getCurrentSession().get(Usuario.class, username);
		try {
			sessionFactory.getCurrentSession().delete(usuario);
			sessionFactory.getCurrentSession().flush();
			model.addAttribute("mensagem", "Usuário excluido com suceso.");
		} catch (Exception e) {
			model.addAttribute("mensagem", "Este usuário não pode ser excluido porque existem tarefas vinculadas a ele.");
		}

		return "forward:listar.action";
	}

	@RequestMapping("/usuario/salvar.action")
	public String salvar(final Model model, final Usuario usuario, int[] papel) throws Exception {
		final Transaction tx = sessionFactory.getCurrentSession().beginTransaction();

		usuario.getPapeis().clear();
		for (int i : papel) {
			usuario.addPapel((Papel) sessionFactory.getCurrentSession().get(Papel.class, i));
		}

		sessionFactory.getCurrentSession().saveOrUpdate(usuario);
		sessionFactory.getCurrentSession().flush();
		tx.commit();
		return "forward:listar.action";
	}

	@RequestMapping("/usuario/trocarSenha.action")
	public String trocarSenha(final Model model, final String username, String password) throws Exception {

		final Usuario usuario = (Usuario) sessionFactory.getCurrentSession().get(Usuario.class, username);
		usuario.setPassword(seguranca.md5(password));

		sessionFactory.getCurrentSession().saveOrUpdate(usuario);
		sessionFactory.getCurrentSession().flush();

		return "redirect:listar.action";

	}

}
