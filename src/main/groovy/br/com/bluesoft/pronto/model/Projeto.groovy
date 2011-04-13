package br.com.bluesoft.pronto.model

import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.SequenceGenerator

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

@Entity
@SequenceGenerator(name = "SEQ_PROJETO", sequenceName = "SEQ_PROJETO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class Projeto {

	@Id
	@GeneratedValue(generator = "SEQ_PROJETO")
	int projetoKey

	String nome

	@Override
	public String toString() {
		return nome
	}
		
}
