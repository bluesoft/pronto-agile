package br.com.bluesoft.pronto.dao;

import java.util.List;

import javax.annotation.PostConstruct;

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
	
	String getProntoUrl() {
		return String.valueOf(this.getMapa()['pronto.url'])
	}
	
	boolean isZendeskAtivo() {
		return String.valueOf(this.getMapa()['zendesk.ativo']).equals('true')
	}
	
	String getZendeskUserName() {
		return String.valueOf(this.getMapa()['zendesk.username'])
	}
	
	String getZendeskPassword() {
		return String.valueOf(this.getMapa()['zendesk.password'])
	}
	
	String getZendeskUrl() {
		return String.valueOf(this.getMapa()['zendesk.url'])
	}
	
	boolean isJabberAtivo() {
		return String.valueOf(this.getMapa()['jabber.ativo']).equals('true')
	}
	
	String getJabberUserName() {
		return String.valueOf(this.getMapa()['jabber.username'])
	}
	
	String getJabberPassword() {
		return String.valueOf(this.getMapa()['jabber.password'])
	}
	
	String getJabberUrl() {
		return String.valueOf(this.getMapa()['jabber.url'])
	}
	
}
