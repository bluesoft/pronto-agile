package br.com.bluesoft.pronto.controller

import static org.springframework.web.bind.annotation.RequestMethod.*

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

import br.com.bluesoft.pronto.core.Papel
import br.com.bluesoft.pronto.dao.UsuarioDao
import br.com.bluesoft.pronto.service.Seguranca
import br.com.bluesoft.pronto.service.StreamService

@Controller
@RequestMapping("/stream")
public class StreamController {
	
	private static final String VIEW_STREAM = "/stream/stream.stream.jsp"
	
	@Autowired StreamService streamService
	@Autowired UsuarioDao usuarioDao
	
	@RequestMapping(value= '/', method = GET)
	String index(final Model model) {
		Seguranca.validarPermissao Papel.PRODUCT_OWNER, Papel.EQUIPE, Papel.SCRUM_MASTER
		model.addAttribute "stream", streamService.listarStream()
		model.addAttribute "usuarios", usuarioDao.listarOrdenadoPorNome()
		VIEW_STREAM
	}
	
	@RequestMapping(value= '/{username}', method = GET)
	String porUsuario(final Model model, @PathVariable String username) {
		Seguranca.validarPermissao Papel.PRODUCT_OWNER, Papel.EQUIPE, Papel.SCRUM_MASTER
		model.addAttribute "stream", streamService.listarStream(username)
		model.addAttribute "usuarios", usuarioDao.listarOrdenadoPorNome()
		model.addAttribute "username", username
		VIEW_STREAM
	}
	
	
	
}
