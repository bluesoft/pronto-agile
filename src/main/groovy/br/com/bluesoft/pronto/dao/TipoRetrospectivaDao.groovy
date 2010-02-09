package br.com.bluesoft.pronto.dao

import org.springframework.stereotype.Repository

import br.com.bluesoft.pronto.core.TipoRetrospectiva

@Repository
public class TipoRetrospectivaDao extends DaoHibernate {

	TipoRetrospectivaDao() {
		super(TipoRetrospectiva.class)
	}

}
