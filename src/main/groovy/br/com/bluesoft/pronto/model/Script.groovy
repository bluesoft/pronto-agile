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
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Pronto. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package br.com.bluesoft.pronto.model

import java.text.MessageFormat
import java.util.Set

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.SequenceGenerator

import com.google.common.collect.Sets

@Entity
@SequenceGenerator(name = "SEQ_SCRIPT", sequenceName = "SEQ_SCRIPT")
class Script {
	
	@Id
	@GeneratedValue(generator = "SEQ_SCRIPT")
	int scriptKey
	
	String descricao
	
	String script
	
	int totalDeExecucoes
	
	int execucoesPendentes
	
	@OneToOne(mappedBy = "script")
	Ticket ticket
	
	@OneToMany(mappedBy = "script", cascade = CascadeType.ALL)
	Set<Execucao> execucoes
	
	Set<BancoDeDados> getBancosDeDados() {
		final Set<BancoDeDados> bancos = Sets.newHashSet()
		if (execucoes != null) {
			for (final Execucao execucao : execucoes) {
				bancos.add(execucao.getBancoDeDados())
			}
		}
		return bancos
	}
	
	boolean temExecucaoParaOBancoDeDados(final int bancoDeDadosKey) {
		if (execucoes != null) {
			for (final Execucao execucao : execucoes) {
				if (execucao.getBancoDeDados().getBancoDeDadosKey() == bancoDeDadosKey) {
					return true
				}
			}
		}
		return false
	}
	
	Execucao adicionarExecucaoParaOBanco(final BancoDeDados bancoDeDados) {
		if (this.execucoes == null) {
			this.execucoes = Sets.newHashSet()
		}
		
		final Execucao execucao = new Execucao()
		execucao.setBancoDeDados(bancoDeDados)
		execucao.setScript(this)
		this.execucoes.add(execucao)
		return execucao
	}
	
	Execucao getExecucaoByBancoDeDados(final int bancoDeDadosKey) {
		if (this.execucoes != null) {
			for (final Execucao execucao : execucoes) {
				if (execucao.getBancoDeDados().getBancoDeDadosKey() == bancoDeDadosKey) {
					return execucao
				}
			}
		}
		return null
	}
	
	@Override
	String toString() {
		return this.scriptKey + " - " + this.getDescricao()
	}
	
	void removerExecucao(final Execucao execucao) {
		if (execucoes != null) {
			execucoes.remove(execucao)
		}
		
	}
	
	Ticket getTicket() {
		return ticket
	}
	
	void setTicket(Ticket ticket) {
		this.ticket = ticket
	}
	
	boolean isTudoExecutado() {
		return execucoesPendentes == 0
	}
	
	String getSituacao() {
		if (totalDeExecucoes == 0) {
			return "Não há Execuções"
		}
		
		if (isTudoExecutado()) {
			return "Tudo Executado"
		} else {
			return MessageFormat.format("{0} de {1} executados", totalDeExecucoes - execucoesPendentes, totalDeExecucoes)
		}
	}
	
}
