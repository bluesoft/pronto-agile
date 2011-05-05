package br.com.bluesoft.pronto.model

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.SequenceGenerator

@Entity
@SequenceGenerator(name = "SEQ_CHECKLIST_ITEM", sequenceName = "SEQ_CHECKLIST_ITEM")
class ChecklistItem {

	@Id
	@GeneratedValue(generator = "SEQ_CHECKLIST_ITEM")
	int checklistItemKey
	
	@ManyToOne(optional=false)
	@JoinColumn(name="checklist_key")
	Checklist checklist

	String descricao

	boolean marcado
	
	def toogle() {
		 this.marcado = !marcado
		 return marcado
	}
}
