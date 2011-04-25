package br.com.bluesoft.pronto.dao

import java.util.List
import org.springframework.stereotype.Repository
import br.com.bluesoft.pronto.model.Projeto

@Repository
class ProjetoDao extends DaoHibernate {

	
	ProjetoDao() {
		super(Projeto.class)
	}

	List<Projeto> listar() {
		String hql = "from Projeto as p order by p.nome"
		return getSession().createQuery(hql).list()
	}
	
	List<Projeto> listarProjetosComSprintsAtivos() {
		String hql = "select distinct p from Projeto as p inner join fetch p.sprints s where s.fechado != true order by p.nome, s.nome"
		return getSession().createQuery(hql).list()
	}
}

