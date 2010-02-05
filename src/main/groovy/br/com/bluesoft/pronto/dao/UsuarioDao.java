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

import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import br.com.bluesoft.pronto.core.Papel;
import br.com.bluesoft.pronto.model.Usuario;

@Repository
public class UsuarioDao extends DaoHibernate<Usuario, String> {

	public UsuarioDao() {
		super(Usuario.class);
	}

	@Override
	public Usuario obter(final String username) {
		final String hql = "select distinct u from Usuario u left join fetch u.papeis where u.username = :username";
		final Query query = getSession().createQuery(hql).setString("username", username);
		return (Usuario) query.uniqueResult();
	}

	public int obterQuantidadeDeUsuariosCadastrados() {
		return getSession().createCriteria(Usuario.class).list().size();
	}

	public String obterPassword(final String username) {
		return (String) getSession().createQuery("select password from Usuario u where u.username = :username").setString("username", username).uniqueResult();

	}

	public List<Usuario> listarEquipe() {
		return listarUsuariosPorPapel(Papel.EQUIPE);
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> listarUsuariosPorPapel(final int papelKey) {
		final String hql = "select distinct u from Usuario u inner join fetch u.papeis p where p.papelKey = :papel order by u.username";
		return getSession().createQuery(hql).setInteger("papel", papelKey).list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Usuario> listar() {
		return getSession().createCriteria(Usuario.class).addOrder(Order.asc("username")).list();
	}
}
