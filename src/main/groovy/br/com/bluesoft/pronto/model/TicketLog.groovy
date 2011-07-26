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
import java.util.Date

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.SequenceGenerator

import br.com.bluesoft.pronto.service.WikiFormatter
import br.com.bluesoft.pronto.util.DateUtil

@Entity
@SequenceGenerator(name = "SEQ_TICKET_LOG", sequenceName = "SEQ_TICKET_LOG")
class TicketLog implements Comparable {
	
	public static final int INCLUSAO = 1
	public static final int ALTERACAO = 2
	public static final int EXCLUSAO = 3
	
	static final String EM_BRANCO = "Em Branco"
	
	@Id
	@GeneratedValue(generator = "SEQ_TICKET_LOG")
	int ticketHistoryKey
	
	@ManyToOne
	@JoinColumn(name = "TICKET_KEY")
	Ticket ticket
	
	String campo
	
	int operacao
	
	String valorAntigo
	
	String valorNovo
	
	Date data
	
	String usuario
	
	
	String getValorAntigo() {
		return valorAntigo == null || valorAntigo.equals("null") ? "Em Branco" : valorAntigo
	}
	
	void setValorAntigo(final String valorAntigo) {
		this.valorAntigo = trataValor(valorAntigo)
		
	}
	
	String getValorNovo() {
		return valorNovo == null || valorNovo.equals("null") ? "Em Branco" : valorNovo
	}
	
	String getDescricaoSemData() {
		this.getDescricao false
	}
	
	String getDescricaoCompleta() {
		this.getDescricao true
	}
	
	String getDescricao(boolean exibirData) {
		
		def dataDaDescricao = exibirData ? this.data : DateUtil.toStringHora(this.data)
		
		switch (getOperacao()) {
			case ALTERACAO: 
				return MessageFormat.format("{0} - {1} - {2} mudou de \"{3}\" para \"{4}\"", dataDaDescricao, usuario, campo, valorAntigo, valorNovo)
			case INCLUSAO:
				if (campo == 'anexo') {
					return MessageFormat.format("{0} - {1} - incluiu o anexo \"{2}\"", dataDaDescricao, usuario, valorNovo)
				} else {
					return MessageFormat.format("{0} - {1} - {2} foi definido como \"{3}\"", dataDaDescricao, usuario, campo, valorNovo)
				}
			case EXCLUSAO:
				if (campo == 'anexo') {
					return MessageFormat.format("{0} - {1} - excluiu o anexo \"{2}\"", dataDaDescricao, usuario, valorAntigo)
				} else {
					return MessageFormat.format("{0} - {1} - excluiu o coment‡rio \"{2}\"", dataDaDescricao, usuario, valorAntigo)
				}
		}
	}
	
	String trataValor(final String input) {
		if (input == null || input.equals("null") || input.length() <= 0) {
			return null
		} else {
			if (input.equals("true")) {
				return "sim"
			} else if (input.equals("false")) {
				return "não"
			} else {
				return input
			}
		}
	}
	
	boolean isDiferente() {
		
		if (getValorAntigo() == null && getValorNovo() == null) {
			return false
		}
		
		if (getValorAntigo() != null && getValorNovo() == null) {
			return true
		}
		
		if (getValorAntigo() == null && getValorNovo() != null) {
			return true
		}
		
		if (getValorAntigo() != null && getValorNovo() != null) {
			return !getValorAntigo().equals(getValorNovo())
		}
		
		return true
		
	}
	
	String getValorAntigoHtml() {
		return WikiFormatter.toHtml(valorAntigo)
	}
	
	String getValorNovoHtml() {
		return WikiFormatter.toHtml(valorNovo)
	}
	
	int compareTo(def outro) {
		if (this.data != null)
			this.data.compareTo(outro.data)
		else
			return 1
	}
}
