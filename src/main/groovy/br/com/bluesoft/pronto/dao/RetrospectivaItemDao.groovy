package br.com.bluesoft.pronto.dao

import org.springframework.stereotype.Repository

import br.com.bluesoft.pronto.model.RetrospectivaItem

@Repository
public class RetrospectivaItemDao extends DaoHibernate {

	RetrospectivaItemDao() {
		super(RetrospectivaItem.class)
	}

}
