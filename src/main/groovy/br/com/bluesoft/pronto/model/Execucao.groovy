/*
 * Copyright 2009 Pronto Agile Project Management.
 *
 * This file is part of Pronto.
 *
 * Pronto is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Pronto is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Pronto. If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
class Execucao {
	
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
	
}
