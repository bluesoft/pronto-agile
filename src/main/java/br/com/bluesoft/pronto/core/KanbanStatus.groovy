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

package br.com.bluesoft.pronto.core

import javax.persistence.Entity
import javax.persistence.Id

@Entity
@org.hibernate.annotations.Entity(mutable = false)
class KanbanStatus {
	
	public static final int TO_DO = 1
	public static final int DOING = 2
	public static final int DONE = 100
	
	@Id
	int kanbanStatusKey
	
	String descricao
	
	KanbanStatus() {
		
	}
	
	KanbanStatus( int kanbanStatusKey) {
		this.kanbanStatusKey = kanbanStatusKey
	}
	
	KanbanStatus( int kanbanStatusKey,  String descricao) {
		this.kanbanStatusKey = kanbanStatusKey
		this.descricao = descricao
	}
	
	String toString() {
		return descricao
	}
	
}
