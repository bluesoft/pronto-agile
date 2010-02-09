package br.com.bluesoft.pronto.dao

import java.util.List

import org.springframework.stereotype.Repository

import br.com.bluesoft.pronto.model.Cliente

@Repository
class ClienteDao extends DaoHibernate {

	ClienteDao() {
		super(Cliente.class)
	}

	List<Cliente> listar() {
		String hql = "from Cliente as c order by c.nome"
		return getSession().createQuery(hql).list()
	}

}
