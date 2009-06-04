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
}
