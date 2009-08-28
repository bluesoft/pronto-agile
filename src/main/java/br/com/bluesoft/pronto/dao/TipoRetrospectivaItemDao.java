package br.com.bluesoft.pronto.dao;

import org.springframework.stereotype.Repository;

import br.com.bluesoft.pronto.core.TipoRetrospectivaItem;

@Repository
public class TipoRetrospectivaItemDao extends DaoHibernate<TipoRetrospectivaItem, Integer> {

	public TipoRetrospectivaItemDao() {
		super(TipoRetrospectivaItem.class);
	}

}
