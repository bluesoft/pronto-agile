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

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.core.TipoRetrospectiva;
import br.com.bluesoft.pronto.dao.RetrospectivaDao;
import br.com.bluesoft.pronto.dao.RetrospectivaItemDao;
import br.com.bluesoft.pronto.dao.SprintDao;
import br.com.bluesoft.pronto.dao.TipoRetrospectivaDao;
import br.com.bluesoft.pronto.dao.TipoRetrospectivaItemDao;
import br.com.bluesoft.pronto.model.Retrospectiva;
import br.com.bluesoft.pronto.model.RetrospectivaItem;
import br.com.bluesoft.pronto.util.ControllerUtil;

@Controller
public class RetrospectivaController {

	@Autowired
	private RetrospectivaDao retrospectivaDao;

	@Autowired
	private TipoRetrospectivaItemDao tipoRetrospectivaItemDao;

	@Autowired
	private RetrospectivaItemDao retrospectivaItemDao;

	@Autowired
	private TipoRetrospectivaDao tipoRetrospectivaDao;

	@Autowired
	private SprintDao sprintDao;

	@RequestMapping("/retrospectiva/ver.action")
	public String retrospectiva(final Model model, final int sprintKey) {
		Retrospectiva retrospectiva = retrospectivaDao.obterRetrospectivaDoSprint(sprintKey);
		if (retrospectiva == null) {
			retrospectiva = new Retrospectiva();
			retrospectiva.setTipoRetrospectiva(tipoRetrospectivaDao.obter(TipoRetrospectiva.TRADICIONAL));
			retrospectiva.setSprint(sprintDao.obter(sprintKey));
		}
		retrospectivaDao.salvar(retrospectiva);

		model.addAttribute("retrospectiva", retrospectiva);
		model.addAttribute("tiposDeRetrospectiva", tipoRetrospectivaDao.listar());
		model.addAttribute("tiposDeItem", tipoRetrospectivaItemDao.listarPorTipoDeRetrospectiva(retrospectiva.getTipoRetrospectiva().getTipoRetrospectivaKey()));
		return "/retrospectiva/retrospectiva.ver.jsp";
	}

	@RequestMapping("/retrospectiva/alterarTipoDeRetrospectiva.action")
	public String alterarTipoDeRetrospectiva(final HttpServletResponse response, final int retrospectivaKey, final int tipoRetrospectivaKey) {
		final Retrospectiva retrospectiva = retrospectivaDao.obter(retrospectivaKey);
		retrospectiva.setTipoRetrospectiva(tipoRetrospectivaDao.obter(tipoRetrospectivaKey));
		retrospectivaDao.salvar(retrospectiva);
		return "/retrospectiva/ver.action?sprintKey=" + retrospectiva.getSprint().getSprintKey();
	}

	@RequestMapping("/retrospectiva/salvarItem.action")
	public void salvarItem(final HttpServletResponse response, final int retrospectivaKey, final int tipoRetrospectivaItemKey, final String descricao) {
		final RetrospectivaItem item = new RetrospectivaItem();
		item.setRetrospectiva(retrospectivaDao.obter(retrospectivaKey));
		item.setTipoRetrospectivaItem(tipoRetrospectivaItemDao.obter(tipoRetrospectivaItemKey));
		item.setDescricao(descricao);
		retrospectivaItemDao.salvar(item);
		ControllerUtil.writeText(response, item.getRetrospectivaItemKey());

	}

	@RequestMapping("/retrospectiva/excluirItem.action")
	public void excluirItem(final HttpServletResponse response, final int retrospectivaItemKey) {
		if (retrospectivaItemKey > 0) {
			retrospectivaItemDao.excluir(retrospectivaItemDao.obter(retrospectivaItemKey));
		}
		ControllerUtil.writeText(response, true);
	}

}
