package br.com.bluesoft.pronto.controller;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.ProntoException;
import br.com.bluesoft.pronto.core.Papel;
import br.com.bluesoft.pronto.dao.PapelDao;
import br.com.bluesoft.pronto.dao.UsuarioDao;
import br.com.bluesoft.pronto.model.Usuario;
import br.com.bluesoft.pronto.service.Seguranca;

@Controller
public class UsuarioController {

	private static final String VIEW_LISTAR = "usuario.listar.jsp";
	private static final String VIEW_EDITAR = "usuario.editar.jsp";

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private UsuarioDao usuarioDao;

	@Autowired
	private PapelDao papelDao;

	@RequestMapping("/usuario/listar.action")
	public String listar(final Model model) {
		final List<Usuario> usuarios = usuarioDao.listar();
		model.addAttribute("usuarios", usuarios);
		return VIEW_LISTAR;
	}

	@RequestMapping("/usuario/editar.action")
	public String editar(final Model model, final String username) throws ProntoException {

		Seguranca.validarPermissao(Papel.SCRUM_MASTER);

		if (username != null) {
			final Usuario usuario = usuarioDao.obter(username);
			model.addAttribute("usuario", usuario);
		} else {
			model.addAttribute("usuario", new Usuario());
		}

		model.addAttribute("papeis", papelDao.listar());

		return VIEW_EDITAR;
	}

	@RequestMapping("/usuario/excluir.action")
	public String excluir(final Model model, final String username) throws ProntoException {

		Seguranca.validarPermissao(Papel.SCRUM_MASTER);

		final int quantidade = usuarioDao.obterQuantidadeDeUsuariosCadastrados();

		if (quantidade == 1) {
			model.addAttribute("mensagem", "Você não pode excluir todos os usuários do Pronto!.");
			return "forward:listar.action";
		}

		final Usuario usuario = usuarioDao.obter(username);
		try {
			sessionFactory.getCurrentSession().delete(usuario);
			sessionFactory.getCurrentSession().flush();
			model.addAttribute("mensagem", "Usuário excluido com suceso.");
		} catch (final Exception e) {
			model.addAttribute("mensagem", "Este usuário não pode ser excluido porque existem tarefas vinculadas a ele.");
		}

		return "forward:listar.action";
	}

	@RequestMapping("/usuario/salvar.action")
	public String salvar(final Model model, final Usuario usuario, final int[] papel, final String password) throws Exception {

		Seguranca.validarPermissao(Papel.SCRUM_MASTER);

		final Transaction tx = sessionFactory.getCurrentSession().beginTransaction();

		final Usuario usuarioAntesDaAlteracao = usuarioDao.obter(usuario.getUsername());
		usuario.setPassword(usuarioAntesDaAlteracao.getPassword());

		usuario.getPapeis().clear();
		for (final int i : papel) {
			usuario.addPapel((Papel) sessionFactory.getCurrentSession().get(Papel.class, i));
		}

		if (password != null) {
			usuario.setPassword(Seguranca.encrypt(password));
		}

		usuarioDao.salvar(usuario);

		tx.commit();
		return "forward:listar.action";
	}

	@RequestMapping("/usuario/digitarSenha.action")
	public String digitarSenha(final Model model, final String username) throws Exception {

		if (!username.equals(Seguranca.getUsuario().getUsername())) {
			Seguranca.validarPermissao(Papel.SCRUM_MASTER);
		}

		final Usuario usuario = usuarioDao.obter(username);
		model.addAttribute("usuario", usuario);

		return "/usuario/usuario.digitarSenha.jsp";

	}

	@RequestMapping("/usuario/trocarSenha.action")
	public String trocarSenha(final Model model, final String username, final String password) throws Exception {

		if (!username.equals(Seguranca.getUsuario().getUsername())) {
			Seguranca.validarPermissao(Papel.SCRUM_MASTER);
		}

		final Usuario usuario = usuarioDao.obter(username);
		usuario.setPassword(Seguranca.encrypt(password));
		usuarioDao.salvar(usuario);

		return "redirect:listar.action";

	}

}
