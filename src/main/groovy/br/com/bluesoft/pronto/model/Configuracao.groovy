package br.com.bluesoft.pronto.model

import javax.persistence.Cacheable;
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name="CONFIGURACOES")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class Configuracao {

	@Id	String chave
	
	String valor
	
}
