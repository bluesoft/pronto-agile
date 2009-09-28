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

import java.util.List;

import javax.servlet.http.HttpSession;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.ProntoException;
import br.com.bluesoft.pronto.core.Papel;
import br.com.bluesoft.pronto.dao.ClienteDao;
import br.com.bluesoft.pronto.dao.PapelDao;
import br.com.bluesoft.pronto.dao.UsuarioDao;
import br.com.bluesoft.pronto.model.Usuario;
import br.com.bluesoft.pronto.service.Seguranca;
import br.com.bluesoft.pronto.util.MD5Util;

@Controller
public class UsuarioController {

	private static final String VIEW_LISTAR = "usuario.listar.jsp";
	private static final String VIEW_EDITAR = "usuario.editar.jsp";

	@Autowired
	private ClienteDao clienteDao;

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

		Seguranca.validarPermissao(Papel.ADMINISTRADOR);

		if (username != null) {
			final Usuario usuario = usuarioDao.obter(username);
			model.addAttribute("usuario", usuario);
		} else {
			model.addAttribute("usuario", new Usuario());
		}

		model.addAttribute("clientes", clienteDao.listar());
		model.addAttribute("papeis", papelDao.listar());

		return VIEW_EDITAR;
	}

	@RequestMapping("/usuario/excluir.action")
	public String excluir(final Model model, final String username) throws ProntoException {

		Seguranca.validarPermissao(Papel.ADMINISTRADOR);

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
	public String salvar(final Model model, final HttpSession httpSession, final Usuario usuario, final int[] papel, final Integer clienteKey, final String password) throws Exception {

		Seguranca.validarPermissao(Papel.ADMINISTRADOR);

		final Transaction tx = sessionFactory.getCurrentSession().beginTransaction();

		usuario.getPapeis().clear();
		for (final int i : papel) {
			usuario.addPapel((Papel) sessionFactory.getCurrentSession().get(Papel.class, i));
		}

		if (usuario.isClientePapel()) {
			usuario.setCliente(clienteDao.obter(clienteKey));
		}

		if (password != null) {
			usuario.setPassword(Seguranca.encrypt(password));
		} else {
			usuario.setPassword(usuarioDao.obterPassword(usuario.getUsername()));
		}

		usuario.setEmailMd5(MD5Util.md5Hex(usuario.getEmail().toLowerCase()));

		usuarioDao.salvar(usuario);

		// atualiza sessão do usuário com os novos papéis
		final Usuario usuarioLogado = (Usuario) httpSession.getAttribute("usuarioLogado");
		if (usuario.getUsername().equals(usuarioLogado.getUsername())) {
			httpSession.setAttribute("usuarioLogado", usuario);
		}

		tx.commit();
		return "forward:listar.action";
	}

	@RequestMapping("/usuario/digitarSenha.action")
	public String digitarSenha(final Model model, final String username) throws Exception {

		if (!username.equals(Seguranca.getUsuario().getUsername())) {
			Seguranca.validarPermissao(Papel.ADMINISTRADOR);
		}

		final Usuario usuario = usuarioDao.obter(username);
		model.addAttribute("usuario", usuario);

		return "/usuario/usuario.digitarSenha.jsp";

	}

	@RequestMapping("/usuario/trocarSenha.action")
	public String trocarSenha(final Model model, final String username, final String password) throws Exception {

		if (!username.equals(Seguranca.getUsuario().getUsername())) {
			Seguranca.validarPermissao(Papel.ADMINISTRADOR);
		}

		final Usuario usuario = usuarioDao.obter(username);
		usuario.setPassword(Seguranca.encrypt(password));
		usuarioDao.salvar(usuario);

		return "redirect:listar.action";

	}

}
