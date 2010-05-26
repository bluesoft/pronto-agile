package br.com.bluesoft.pronto.model

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "SEQ_CAUSA_DE_DEFEITO", sequenceName = "SEQ_CAUSA_DE_DEFEITO")
class CausaDeDefeito implements Comparable {
	
	@Id @GeneratedValue(generator = "SEQ_CAUSA_DE_DEFEITO")
	int causaDeDefeitoKey
	
	String descricao
	
	String toString() {
		this.descricao
	}
	
	int compareTo(def outro) {
		this.descricao.compareTo outro.descricao
	}
	
}
