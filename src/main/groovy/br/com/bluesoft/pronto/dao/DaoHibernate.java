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

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class DaoHibernate<T, K extends Serializable> {

	@Autowired
	private SessionFactory sessionFactory;
	private final Class<T> clazz;

	public DaoHibernate(final Class<T> clazz) {
		this.clazz = clazz;
	}

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public T obter(final K key) {
		return (T) getSession().get(clazz, key);
	}

	@SuppressWarnings("unchecked")
	public List<T> listar() {
		return this.getSession().createCriteria(clazz).list();
	}

	public void salvar(final T... ts) {
		for (final T t : ts) {
			getSession().saveOrUpdate(t);
		}
		getSession().flush();
	}

	public void excluir(final T... ts) {
		for (final T t : ts) {
			getSession().delete(t);
		}
		getSession().flush();
	}

}
