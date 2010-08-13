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

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Entity
import javax.persistence.Id

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cacheable
@org.hibernate.annotations.Entity(mutable = false)
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "eternal")
class Papel implements Serializable{
	
	public static final int ADMINISTRADOR = 6
	public static final int PRODUCT_OWNER = 1
	public static final int SCRUM_MASTER = 2
	public static final int EQUIPE = 9
	public static final int CLIENTE = 7
	
	Papel() {
	}
	
	Papel( int papelKey) {
		this.papelKey = papelKey
	}
	
	Papel( int papelKey,  String descricao) {
		this.papelKey = papelKey
		this.descricao = descricao
	}
	
	@Id
	int papelKey
	
	String descricao
	
	@Override
	int hashCode() {
		papelKey.hashCode()
	}
	
	@Override
	boolean equals(Object obj) {
		papelKey.equals obj.papelKey
	}
	
	@Override
	String toString() {
		return descricao
	}
}
