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
public class TipoRetrospectiva {

	public static int TRADICIONAL = 1;
	public static int SEIS_CHAPEUS = 2;

	@Id
	private int tipoRetrospectivaKey;

	private String descricao;

	public int getTipoRetrospectivaKey() {
		return tipoRetrospectivaKey;
	}

	public void setTipoRetrospectivaKey(final int tipoRetrospectivaKey) {
		this.tipoRetrospectivaKey = tipoRetrospectivaKey;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

}
