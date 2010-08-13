package br.com.bluesoft.pronto.model

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@SequenceGenerator(name = "SEQ_CAUSA_DE_DEFEITO", sequenceName = "SEQ_CAUSA_DE_DEFEITO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class CausaDeDefeito implements Comparable {
	
	@Id @GeneratedValue(generator = "SEQ_CAUSA_DE_DEFEITO")
	int causaDeDefeitoKey
	
	String descricao
	
	String toString() {
		this.descricao
	}
	
	int compareTo(def outro) {
		this.descricao.compareTo outro.descricao
	}
	
}
