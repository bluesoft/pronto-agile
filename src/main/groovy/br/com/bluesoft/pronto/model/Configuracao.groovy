package br.com.bluesoft.pronto.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name="CONFIGURACOES")
class Configuracao {

	@Id	String chave
	
	String valor
	
}
