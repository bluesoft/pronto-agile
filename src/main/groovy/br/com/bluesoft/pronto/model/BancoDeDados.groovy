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

import java.util.List
import java.util.Set

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.SequenceGenerator

import com.google.common.base.Predicate
import com.google.common.collect.Iterables
import com.google.common.collect.Lists

@Entity
@SequenceGenerator(name = "SEQ_BANCO_DE_DADOS", sequenceName = "SEQ_BANCO_DE_DADOS")
class BancoDeDados {
	
	@Id
	@GeneratedValue(generator = "SEQ_BANCO_DE_DADOS")
	int bancoDeDadosKey
	
	String nome
	
	@OneToMany(mappedBy = "bancoDeDados", cascade = CascadeType.REMOVE)
	Set<Execucao> execucoes
	
	String toString() {
		this.nome
	}
	
	List<Execucao> getExecucoesPendentes() {
		execucoes.findAll { it.data == null } as List
	}
	
}
