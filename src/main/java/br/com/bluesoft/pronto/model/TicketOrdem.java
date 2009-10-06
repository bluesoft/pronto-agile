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

public enum TicketOrdem {

	/**/CODIGO("Código"),
	/**/TITULO("Título"),
	/**/TIPO("Tipo"),
	/**/CLIENTE("Cliente"),
	/**/BACKLOG("Backlog"),
	/**/VALOR_DE_NEGOCIO("Valor de Negócio"),
	/**/ESFORCO("Esforço"),
	/**/STATUS("Status");

	private String descricao;

	private TicketOrdem(final String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
