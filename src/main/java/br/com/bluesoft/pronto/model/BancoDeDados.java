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

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "SEQ_BANCO_DE_DADOS", sequenceName = "SEQ_BANCO_DE_DADOS")
public class BancoDeDados {

	@Id
	@GeneratedValue(generator = "SEQ_BANCO_DE_DADOS")
	private int bancoDeDadosKey;

	private String nome;

	@OneToMany(mappedBy = "bancoDeDados")
	private Set<Execucao> execucoes;

	public int getBancoDeDadosKey() {
		return bancoDeDadosKey;
	}

	public void setBancoDeDadosKey(final int bancoDeDadosKey) {
		this.bancoDeDadosKey = bancoDeDadosKey;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(final String nome) {
		this.nome = nome;
	}

	public Set<Execucao> getExecucoes() {
		return execucoes;
	}

	public void setExecucoes(final Set<Execucao> execucoes) {
		this.execucoes = execucoes;
	}

	@Override
	public String toString() {
		return this.nome;
	}

}
