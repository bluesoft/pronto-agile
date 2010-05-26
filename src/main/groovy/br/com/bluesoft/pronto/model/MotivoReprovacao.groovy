package br.com.bluesoft.pronto.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import br.com.bluesoft.pronto.core.Cor;

@Entity
@SequenceGenerator(name = "SEQ_MOTIVO_REPROVACAO", sequenceName = "SEQ_MOTIVO_REPROVACAO")
class MotivoReprovacao implements Comparable {
	
	@Id @GeneratedValue(generator = "SEQ_MOTIVO_REPROVACAO")
	int motivoReprovacaoKey	
	
	String descricao	
	
	int compareTo(def outro) {
		this.descricao.compareTo outro.descricao
	}
	
	String toString() {
		this.descricao
	}

}
