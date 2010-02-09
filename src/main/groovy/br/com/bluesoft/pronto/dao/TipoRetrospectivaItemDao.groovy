package br.com.bluesoft.pronto.dao

import java.util.List

import org.springframework.stereotype.Repository

import br.com.bluesoft.pronto.core.TipoRetrospectivaItem

@Repository
public class TipoRetrospectivaItemDao extends DaoHibernate{

	public TipoRetrospectivaItemDao() {
		super(TipoRetrospectivaItem.class)
	}

	public List<TipoRetrospectivaItem> listarPorTipoDeRetrospectiva(final int tipoRetrospectivaKey) {
		final String hql = "from TipoRetrospectivaItem t where t.tipoRetrospectiva.tipoRetrospectivaKey = :tipoRetrospectivaKey"
		return getSession().createQuery(hql).setInteger("tipoRetrospectivaKey", tipoRetrospectivaKey).list()
	}

}
