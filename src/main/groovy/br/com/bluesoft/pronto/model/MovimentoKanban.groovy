package br.com.bluesoft.pronto.model

import javax.persistence.ManyToOne
import javax.persistence.ManyToOne
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.SequenceGenerator

import br.com.bluesoft.pronto.core.KanbanStatus;

@Entity
@SequenceGenerator(name = "SEQ_MOVIMENTO_KANBAN", sequenceName = "SEQ_MOVIMENTO_KANBAN")
class MovimentoKanban {
	
	@Id @GeneratedValue(generator = "SEQ_MOVIMENTO_KANBAN")
	int movimentoKanbanKey
	
	@ManyToOne
	@JoinColumn(name = "ticket_key")
	Ticket ticket
	
	@ManyToOne
	@JoinColumn(name = "motivo_reprovacao_key")
	MotivoReprovacao motivoReprovacao
	
	Date data
	
	@ManyToOne
	@JoinColumn(name = "username")
	Usuario usuario
	
	@ManyToOne
	@JoinColumn(name = "kanban_status_key")
	KanbanStatus kanbanStatus
	
	String getDescricao() {
		if (motivoReprovacao)
			"${usuario.username} voltou para ${kanbanStatus.descricao} em ${data} por ${motivoReprovacao.descricao}"
		else
			"${usuario.username} moveu para ${kanbanStatus.descricao} em ${data}"			
	}
	
}