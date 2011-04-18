package br.com.bluesoft.pronto.core

import javax.persistence.Cacheable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.SequenceGenerator

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

import br.com.bluesoft.pronto.model.Projeto

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SequenceGenerator(name = "SEQ_KANBAN_STATUS", sequenceName = "SEQ_KANBAN_STATUS")
class KanbanStatus implements Comparable {

	@Id 
	@GeneratedValue(generator = "SEQ_KANBAN_STATUS")
	Integer kanbanStatusKey
	
	@ManyToOne
	@JoinColumn(name="PROJETO_KEY")
	Projeto projeto
	
	String descricao
	boolean inicio
	boolean fim
	int ordem

	KanbanStatus() {
	}

	KanbanStatus( int kanbanStatusKey) {
		this.kanbanStatusKey = kanbanStatusKey
	}

	KanbanStatus( int kanbanStatusKey,  String descricao) {
		this.kanbanStatusKey = kanbanStatusKey
		this.descricao = descricao
	}

	String toString() {
		return descricao
	}

	int compareTo(def outro) {
		this.ordem.compareTo outro.ordem
	}
}