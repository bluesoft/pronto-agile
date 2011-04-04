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

import java.io.Serializable
import java.util.HashMap
import java.util.HashSet
import java.util.Map
import java.util.Set

import javax.persistence.Cacheable;
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.bluesoft.pronto.core.Papel


@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class Usuario implements Comparable<Usuario>, Serializable {
	
	@Id
	String username
	
	String password
	
	String nome
	
	String email
	
	String emailMd5
	
	String telefone
	
	String jabberUsername
	
	@ManyToOne
	@JoinColumn(name = "CLIENTE_KEY")
	Cliente cliente
	
	@ManyToMany
	@JoinTable(name = "USUARIO_PAPEL", joinColumns =  @JoinColumn(name = "USUARIO_KEY"), inverseJoinColumns =  @JoinColumn(name = "PAPEL_KEY") )
	Set<Papel> papeis = new HashSet<Papel>()
	
	void addPapel(final Papel papel) {
		papeis.add(papel)
	}
	
	Map<Integer, Boolean> getMapaPapeis() {
		final Map<Integer, Boolean> mapaPapeis = new HashMap<Integer, Boolean>()
		for (final Papel papel : papeis) {
			mapaPapeis.put(papel.getPapelKey(), true)
		}
		return mapaPapeis
	}
	
	String toString() {
		return username
	}
	
	@Override
	int hashCode() {
		final int prime = 31
		int result = 1
		result = prime * result + (username == null ? 0 : username.hashCode())
		return result
	}
	
	@Override
	boolean equals(final Object obj) {
		if (this == obj) {
			return true
		}
		if (obj == null) {
			return false
		}
		if (getClass() != obj.getClass()) {
			return false
		}
		final Usuario other = (Usuario) obj
		if (username == null) {
			if (other.username != null) {
				return false
			}
		} else if (!username.equals(other.username)) {
			return false
		}
		return true
	}
	
	int compareTo(def outro) {
		return username.compareTo(outro.username)
	}
	
	boolean isProductOwner() {
		return temOPapel(Papel.PRODUCT_OWNER)
	}
	
	boolean isScrumMaster() {
		return temOPapel(Papel.SCRUM_MASTER)
	}
	
	boolean isEquipe() {
		return temOPapel(Papel.EQUIPE)
	}
	
	boolean isAdministrador() {
		return temOPapel(Papel.ADMINISTRADOR)
	}
	
	boolean isClientePapel() {
		return temOPapel(Papel.CLIENTE)
	}
	
	boolean temOPapel(final int papelKey) {
		for (final Papel papel : papeis) {
			if (papel.getPapelKey() == papelKey) {
				return true
			}
		}
		return false
	}
	
	boolean hasJabber() {
		return this.jabberUsername != null && this.jabberUsername.length() > 0 
	}
	
}
