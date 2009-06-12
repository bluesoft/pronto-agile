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

}
