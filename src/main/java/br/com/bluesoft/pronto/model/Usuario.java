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

package br.com.bluesoft.pronto.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import br.com.bluesoft.pronto.core.Papel;

@Entity
public class Usuario implements Comparable<Usuario> {

	@Id
	private String username;

	private String password;

	private String nome;

	private String email;

	private String emailMd5;

	@ManyToMany
	@JoinTable(name = "USUARIO_PAPEL", joinColumns = { @JoinColumn(name = "USUARIO_KEY") }, inverseJoinColumns = { @JoinColumn(name = "PAPEL_KEY") })
	private Set<Papel> papeis = new HashSet<Papel>();

	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(final String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getEmailMd5() {
		return emailMd5;
	}

	public void setEmailMd5(final String emailMd5) {
		this.emailMd5 = emailMd5;
	}

	public Set<Papel> getPapeis() {
		return papeis;
	}

	public void setPapeis(final Set<Papel> papeis) {
		this.papeis = papeis;
	}

	public void addPapel(final Papel papel) {
		papeis.add(papel);
	}

	public Map<Integer, Boolean> getMapaPapeis() {
		final Map<Integer, Boolean> mapaPapeis = new HashMap<Integer, Boolean>();
		for (final Papel papel : papeis) {
			mapaPapeis.put(papel.getPapelKey(), true);
		}
		return mapaPapeis;
	}

	@Override
	public String toString() {
		return username;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (username == null ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Usuario other = (Usuario) obj;
		if (username == null) {
			if (other.username != null) {
				return false;
			}
		} else if (!username.equals(other.username)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(final Usuario outro) {
		return username.compareTo(outro.username);
	}

	public boolean isProductOwner() {
		return temOPapel(Papel.PRODUCT_OWNER);
	}

	public boolean isDesenvolvedor() {
		return temOPapel(Papel.DESENVOLVEDOR);
	}

	public boolean isScrumMaster() {
		return temOPapel(Papel.SCRUM_MASTER);
	}

	public boolean isSuporte() {
		return temOPapel(Papel.SUPORTE);
	}

	public boolean isTestador() {
		return temOPapel(Papel.TESTADOR);
	}

	public boolean temOPapel(final int papelKey) {
		for (final Papel papel : papeis) {
			if (papel.getPapelKey() == papelKey) {
				return true;
			}
		}
		return false;
	}

}
