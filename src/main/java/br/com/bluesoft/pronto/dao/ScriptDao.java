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
package br.com.bluesoft.pronto.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.bluesoft.pronto.model.Execucao;
import br.com.bluesoft.pronto.model.Script;

@Repository
public class ScriptDao extends DaoHibernate<Script, Integer> {

	public ScriptDao() {
		super(Script.class);
	}

	public List<Script> listarPendentes() {
		return listar();
	}

	public void removerExecucoesDoScript(final Script script) {
		if (script.getExecucoes() != null) {
			for (final Execucao execucao : script.getExecucoes()) {
				removerExecucao(execucao);
			}
		}
	}

	public void removerExecucao(final Execucao execucao) {
		final Script script = execucao.getScript();
		script.removerExecucao(execucao);
		getSession().delete(execucao);
	}

}
