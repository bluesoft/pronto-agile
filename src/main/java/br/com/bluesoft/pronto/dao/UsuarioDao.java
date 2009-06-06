package br.com.bluesoft.pronto.dao;

import org.springframework.stereotype.Repository;

import br.com.bluesoft.pronto.model.Usuario;

@Repository
public class UsuarioDao extends DaoHibernate<Usuario, String> {

	public UsuarioDao() {
		super(Usuario.class);
	}

	public int obterQuantidadeDeUsuariosCadastrados() {
		return getSession().createCriteria(Usuario.class).list().size();
	}

	public String obterPassword(final String username) {
		return (String) getSession().createQuery("select password from Usuario u where u.username = :username").setString("username", username).uniqueResult();

	}
}
