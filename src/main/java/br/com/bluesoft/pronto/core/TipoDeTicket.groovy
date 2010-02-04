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

package br.com.bluesoft.pronto.core;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
class TipoDeTicket {
	
	public static final int IDEIA = 1
	public static final int ESTORIA = 2
	public static final int DEFEITO = 3
	public static final int TAREFA = 6
	
	@Id
	int tipoDeTicketKey
	
	String descricao
	
	public TipoDeTicket() {
	}
	
	public TipoDeTicket( int tipoDeTicketKey,  String descricao) {
		this.tipoDeTicketKey = tipoDeTicketKey
		this.descricao = descricao
	}
	
	public TipoDeTicket( int tipoDeTicketKey) {
		this.tipoDeTicketKey = tipoDeTicketKey
	}
	
	
}
