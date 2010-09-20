package br.com.bluesoft.pronto.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.bluesoft.pronto.core.Cor;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SequenceGenerator(name = "SEQ_MODULO", sequenceName = "SEQ_MODULO")
class Modulo implements Comparable {
	
	@Id @GeneratedValue(generator = "SEQ_MODULO")
	int moduloKey
	
	String descricao
	
	int compareTo(def outro) {
		this.descricao.compareTo outro.descricao
	}
	
	String toString() {
		this.descricao
	}
	

}
