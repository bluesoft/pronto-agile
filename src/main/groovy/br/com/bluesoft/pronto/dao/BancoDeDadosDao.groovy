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

import br.com.bluesoft.pronto.model.BancoDeDados;

@Repository
public class BancoDeDadosDao extends DaoHibernate<BancoDeDados, Integer> {

	public BancoDeDadosDao() {
		super(BancoDeDados.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BancoDeDados> listar() {
		return getSession().createQuery("from BancoDeDados b order by b.nome").list();
	}

}
