package br.com.bluesoft.pronto.model

import javax.persistence.Cacheable
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OrderBy
import javax.persistence.SequenceGenerator

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

import br.com.bluesoft.pronto.core.KanbanStatus

@Entity
@SequenceGenerator(name = "SEQ_PROJETO", sequenceName = "SEQ_PROJETO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class Projeto {

	@Id
	@GeneratedValue(generator = "SEQ_PROJETO")
	int projetoKey

	String nome

	@OneToMany(mappedBy="projeto", cascade=[CascadeType.ALL], orphanRemoval=true)
	@OrderBy("ordem")
	List<KanbanStatus> etapasDoKanban = []
	
	@OneToMany(mappedBy="projeto")
	@OrderBy("nome")
	List<Sprint> sprints = []

	@Override
	public String toString() {
		return nome
	}
	
	KanbanStatus getEtapaToDo() {
		etapasDoKanban.find { it.inicio }
	}
}
