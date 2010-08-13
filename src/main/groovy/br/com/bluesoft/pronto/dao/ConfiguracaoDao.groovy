package br.com.bluesoft.pronto.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.bluesoft.pronto.model.Configuracao;

@Repository
class ConfiguracaoDao {
	
	@Autowired SessionFactory sessionFactory
	
	private Session getSession() {
		return sessionFactory.getCurrentSession()
	}

	private Map cache = null;
	
	
	Map getMapa() {
		if (cache == null) {
			def mapa = [:]
			def lista = getSession().createCriteria(Configuracao.class).list();
			lista.each {
				mapa[it.chave] = it.valor
			}
			this.cache = mapa;
		}
		return this.cache
	}
	
	void atualizarConfiguracoes(def mapa) {
		def tx = session.beginTransaction()
		mapa.each { entry ->
			if (cache[entry.key] != null) {
				cache[entry.key] = entry.value[0]
				def configuracao = getSession().get(Configuracao.class, entry.key)
				configuracao.valor = String.valueOf(entry.value[0])
				getSession().update configuracao
				getSession().flush()
			} else {
				cache[entry.key] = entry.value[0]
				getSession().save(new Configuracao(chave:entry.key, valor:String.valueOf(entry.value[0])))
				getSession().flush()
			}
		}
		tx.commit()
	}
	
	boolean isZenDeskAtivo() {
		return String.valueOf(this.getMapa()['zenDesk.ativo']).equals('true')
	}
	
	String getZenDeskUserName() {
		return String.valueOf(this.getMapa()['zenDesk.username'])
	}
	
	String getZenDeskPassword() {
		return String.valueOf(this.getMapa()['zenDesk.password'])
	}
	
	String getZenDeskUrl() {
		return String.valueOf(this.getMapa()['zenDesk.url'])
	}
	
}
