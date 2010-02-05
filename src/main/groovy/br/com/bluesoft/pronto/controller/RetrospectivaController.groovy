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

import javax.servlet.http.HttpServletResponse

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.bluesoft.pronto.core.TipoRetrospectiva
import br.com.bluesoft.pronto.dao.RetrospectivaDao
import br.com.bluesoft.pronto.dao.RetrospectivaItemDao
import br.com.bluesoft.pronto.dao.SprintDao
import br.com.bluesoft.pronto.dao.TipoRetrospectivaDao
import br.com.bluesoft.pronto.dao.TipoRetrospectivaItemDao
import br.com.bluesoft.pronto.model.Retrospectiva
import br.com.bluesoft.pronto.model.RetrospectivaItem
import br.com.bluesoft.pronto.util.ControllerUtil
import static org.springframework.web.bind.annotation.RequestMethod.*

@Controller
@RequestMapping("/retrospectivas")
class RetrospectivaController {
	
	@Autowired RetrospectivaDao retrospectivaDao
	
	@Autowired TipoRetrospectivaItemDao tipoRetrospectivaItemDao
	
	@Autowired RetrospectivaItemDao retrospectivaItemDao
	
	@Autowired TipoRetrospectivaDao tipoRetrospectivaDao
	
	@Autowired SprintDao sprintDao
	
	@RequestMapping("/sprints/{sprintKey}")
	String retrospectiva( Model model, @PathVariable int sprintKey) {
		Retrospectiva retrospectiva = retrospectivaDao.obterRetrospectivaDoSprint(sprintKey)
		if (retrospectiva == null) {
			retrospectiva = new Retrospectiva()
			retrospectiva.setTipoRetrospectiva tipoRetrospectivaDao.obter(TipoRetrospectiva.TRADICIONAL)
			retrospectiva.setSprint sprintDao.obter(sprintKey)
		}
		retrospectivaDao.salvar retrospectiva
		
		model.addAttribute "retrospectiva", retrospectiva
		model.addAttribute "tiposDeRetrospectiva", tipoRetrospectivaDao.listar()
		model.addAttribute "tiposDeItem", tipoRetrospectivaItemDao.listarPorTipoDeRetrospectiva(retrospectiva.getTipoRetrospectiva().getTipoRetrospectivaKey())
		"/retrospectiva/retrospectiva.ver.jsp"
	}
	
	@RequestMapping("/{retrospectivaKey}/alterarTipoDeRetrospectiva")
	String alterarTipoDeRetrospectiva( HttpServletResponse response, @PathVariable int retrospectivaKey,  int tipoRetrospectivaKey) {
		Retrospectiva retrospectiva = retrospectivaDao.obter(retrospectivaKey)
		retrospectiva.setTipoRetrospectiva(tipoRetrospectivaDao.obter(tipoRetrospectivaKey))
		retrospectivaDao.salvar(retrospectiva)
		"redirect:/retrospectivas/sprints/${retrospectiva.sprint.sprintKey}"
	}
	
	@RequestMapping(value = '/{retrospectivaKey}', method=[POST, PUT])
	@ResponseBody String salvarItem( HttpServletResponse response, @PathVariable int retrospectivaKey,  int tipoRetrospectivaItemKey,  String descricao) {
		RetrospectivaItem item = new RetrospectivaItem()
		item.setRetrospectiva(retrospectivaDao.obter(retrospectivaKey))
		item.setTipoRetrospectivaItem(tipoRetrospectivaItemDao.obter(tipoRetrospectivaItemKey))
		item.setDescricao(descricao)
		retrospectivaItemDao.salvar(item)
		item.getRetrospectivaItemKey().toString()
	}
	
	@RequestMapping(value="itens/{retrospectivaItemKey}", method=DELETE)
	@ResponseBody String excluirItem( HttpServletResponse response,  @PathVariable int retrospectivaItemKey) {
		if (retrospectivaItemKey > 0) {
			retrospectivaItemDao.excluir(retrospectivaItemDao.obter(retrospectivaItemKey))
		}
		true.toString()
	}
	
}
