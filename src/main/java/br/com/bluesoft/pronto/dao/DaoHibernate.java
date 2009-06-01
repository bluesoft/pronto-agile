package br.com.bluesoft.pronto.dao;

import java.io.Serializable;

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

	public void salvar(final T t) {
		getSession().saveOrUpdate(t);
		getSession().flush();
	}

}
