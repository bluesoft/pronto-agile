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

package br.com.bluesoft.pronto.controller


import javax.servlet.http.HttpSession

import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

import br.com.bluesoft.pronto.core.Papel
import br.com.bluesoft.pronto.dao.ClienteDao
import br.com.bluesoft.pronto.dao.PapelDao
import br.com.bluesoft.pronto.dao.UsuarioDao
import br.com.bluesoft.pronto.model.Usuario
import br.com.bluesoft.pronto.service.Seguranca
import br.com.bluesoft.pronto.util.MD5Util

import static org.springframework.web.bind.annotation.RequestMethod.*

@Controller
@RequestMapping("/usuarios")
class UsuarioController {
	
	private static final String VIEW_LISTAR = "/usuario/usuario.listar.jsp"
	private static final String VIEW_EDITAR = "/usuario/usuario.editar.jsp"
	
	@Autowired ClienteDao clienteDao
	
	@Autowired SessionFactory sessionFactory
	
	@Autowired UsuarioDao usuarioDao
	
	@Autowired PapelDao papelDao
	
	@RequestMapping(method = GET)
	String listar(final Model model) {
		model.addAttribute("usuarios", usuarioDao.listar())
		VIEW_LISTAR
	}
	
	@RequestMapping(value = "/novo", method = GET)
	String novo(final Model model) {
		editar(model, null)
	}
	
	@RequestMapping(value = "/{username}", method = GET)
	String editar(final Model model, @PathVariable final String username)  {
		
		Seguranca.validarPermissao Papel.ADMINISTRADOR
		
		model.addAttribute("usuario", username ? usuarioDao.obter(username) : new Usuario())
		model.addAttribute("clientes", clienteDao.listar())
		model.addAttribute("papeis", papelDao.listar())
		
		VIEW_EDITAR
	}
	
	@RequestMapping(value = "/{username}", method = DELETE)
	String excluir(final Model model, final @PathVariable String username) {
		
		Seguranca.validarPermissao Papel.ADMINISTRADOR
		
		def quantidade = usuarioDao.obterQuantidadeDeUsuariosCadastrados()
		
		if (quantidade == 1) {
			model.addAttribute("mensagem", "Você não pode excluir todos os usuários do Pronto!")
			return "forward:/app/usuarios"
		}
		
		def usuario = usuarioDao.obter(username)
		try {
			sessionFactory.currentSession.delete(usuario)
			sessionFactory.currentSession.flush()
			model.addAttribute("mensagem", "Usuário excluído com sucesso.")
		} catch (final Exception e) {
			model.addAttribute("mensagem", "Este usuário não pode ser excluído porque existem tarefas vinculadas a ele.")
		}
		
		return "redirect:/usuarios"
	}
	
	@RequestMapping(method = [ POST, PUT ])
	String salvar(final Model model, final HttpSession httpSession, final Usuario usuario, final int[] papel, final Integer clienteKey, final String password) throws Exception {
		
		Seguranca.validarPermissao Papel.ADMINISTRADOR
		
		final Transaction tx = sessionFactory.currentSession.beginTransaction()
		usuario.getPapeis().clear()
		
		for (final int i : papel) {
			usuario.addPapel((Papel) sessionFactory.currentSession.get(Papel.class, i))
		}
		
		if (usuario.isClientePapel()) {
			usuario.cliente  = clienteDao.obter(clienteKey)
		}
		
		if (password != null) {
			usuario.password = Seguranca.encrypt(password)
		} else {
			usuario.password = usuarioDao.obterPassword(usuario.getUsername())
		}
		
		usuario.setEmailMd5 MD5Util.md5Hex(usuario.getEmail().toLowerCase())
		
		usuarioDao.salvar(usuario)
		
		// atualiza sessão do usuário com os novos papéis
		Usuario usuarioLogado = (Usuario) httpSession.getAttribute("usuarioLogado")
		if (usuario.username == usuarioLogado.username) {
			httpSession.setAttribute "usuarioLogado", usuario
		}
		
		tx.commit()
		
		"redirect:/usuarios"
	}
	
	@RequestMapping(value = "/{username}/trocarSenha", method = GET)
	String digitarSenha(final Model model, @PathVariable final String username) throws Exception {
		
		if (username != Seguranca.usuario.username) {
			Seguranca.validarPermissao Papel.ADMINISTRADOR
		}
		
		model.addAttribute "usuario", usuarioDao.obter(username)
		
		"/usuario/usuario.digitarSenha.jsp"
		
	}
	
	@RequestMapping(value = "/{username}/trocarSenha", method = PUT)
	String trocarSenha(final Model model, @PathVariable final String username, final String password) throws Exception {
		
		if (username != Seguranca.usuario.username) {
			Seguranca.validarPermissao Papel.ADMINISTRADOR
		}
		
		def usuario = usuarioDao.obter(username)
		usuario.password = Seguranca.encrypt(password)
		usuarioDao.salvar usuario
		
		"redirect:/usuarios"
	}
	
}
