package br.com.bluesoft.pronto.model

import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.SequenceGenerator

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy


@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SequenceGenerator(name = "SEQ_MILESTONE", sequenceName = "SEQ_MILESTONE")
class Milestone implements Comparable {
	
	@Id
	@GeneratedValue(generator = "SEQ_MILESTONE")
	Integer milestoneKey
	
	@ManyToOne
	@JoinColumn(name="PROJETO_KEY")
	Projeto projeto
	
	String nome
	
	int compareTo(def outro) {
		this.nome.compareTo outro.nome
	}
	
	public String toString() {
		nome
	}

}
