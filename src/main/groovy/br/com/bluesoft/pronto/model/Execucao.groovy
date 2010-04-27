package br.com.bluesoft.pronto.model

import java.text.MessageFormat
import java.util.Date

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.SequenceGenerator
import javax.persistence.Temporal
import javax.persistence.TemporalType

@Entity
@SequenceGenerator(name = "SEQ_EXECUCAO", sequenceName = "SEQ_EXECUCAO")
class Execucao implements Comparable  {
	
	@Id
	@GeneratedValue(generator = "SEQ_EXECUCAO")
	int execucaoKey
	
	@ManyToOne
	@JoinColumn(name = "BANCO_DE_DADOS_KEY")
	BancoDeDados bancoDeDados
	
	@ManyToOne
	@JoinColumn(name = "SCRIPT_KEY")
	Script script
	
	@ManyToOne
	@JoinColumn(name = "USERNAME")
	Usuario usuario
	
	@Temporal(TemporalType.TIMESTAMP)
	Date data
	
	boolean isExecutado() {
		return this.data != null
	}
	
	boolean isPendente() {
		return this.data == null
	}
	
	String getStatus() {
		isExecutado() ? "Executado em ${data} por ${usuario.username}" : "Não Executado"
	}
	
	int compareTo(def outro) {
		this.script.scriptKey.compareTo script.scriptKey
	}
	
}
