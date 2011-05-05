package br.com.bluesoft.pronto.model

import javax.persistence.CascadeType;
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.SequenceGenerator


@Entity
@SequenceGenerator(name = "SEQ_CHECKLIST", sequenceName = "SEQ_CHECKLIST")
class Checklist {

	@Id
	@GeneratedValue(generator = "SEQ_CHECKLIST")
	Integer checklistKey

	String nome

	@ManyToOne
	@JoinColumn(name="ticket_key")
	Ticket ticket

	@OneToMany(mappedBy="checklist", orphanRemoval=true, cascade=CascadeType.ALL)
	List<ChecklistItem> itens = []
}
