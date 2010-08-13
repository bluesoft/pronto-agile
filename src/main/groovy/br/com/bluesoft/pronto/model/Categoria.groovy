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
@SequenceGenerator(name = "SEQ_CATEGORIA", sequenceName = "SEQ_CATEGORIA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class Categoria implements Comparable {
	
	@Id @GeneratedValue(generator = "SEQ_CATEGORIA")
	int categoriaKey
	
	String descricao
	
	String cor
	
	int compareTo(def outro) {
		this.descricao.compareTo outro.descricao
	}
	
	String getDescricaoDaCor() {
		if (cor != null) {
			Cor.valueOf(this.cor).getDescricao()
		}
	}
	
	String toString() {
		this.descricao
	}
	

}
