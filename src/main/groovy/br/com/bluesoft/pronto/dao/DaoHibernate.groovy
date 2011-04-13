package br.com.bluesoft.pronto.dao

import java.io.Serializable
import java.lang.reflect.ParameterizedType
import java.util.List

import org.hibernate.Session
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired

class DaoHibernate {
	
	final Class clazz
	
	DaoHibernate(final Class clazz) {
		this.clazz = clazz
	}
	
	@Autowired SessionFactory sessionFactory
	
	protected Session getSession() {
		return sessionFactory.getCurrentSession()
	}

	Object obter(def key) {
		return getSession().get(clazz, key)
	}
	
	Object proxy(def key) {
		return getSession().load(clazz, key)
	}

	List listar() {
		return this.getSession().createCriteria(clazz).list()
	}

	void salvar(Object... ts) {
		for (final Object t : ts) {
			getSession().saveOrUpdate(t)
		}
		getSession().flush()
	}

	void excluir(Object... ts) {
		for (final Object t : ts) {
			getSession().delete(t)
		}
		getSession().flush()
	}
	
	List buscar(String hql, Object... parameters) {
		def query = getSession().createQuery(hql)
		parameters?.eachWithIndex { parameter, index ->
			query.setParameter index, parameter
		}
		query.list()
	}

}
