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
public class TipoRetrospectivaItem {

	public static final int WWW = 1;
	public static final int WCBI = 2;

	@Id
	private int tipoRetrospectivaItemKey;

	private String descricao;

	public int getTipoRetrospectivaItemKey() {
		return tipoRetrospectivaItemKey;
	}

	public void setTipoRetrospectivaItemKey(final int tipoRetrospectivaItemKey) {
		this.tipoRetrospectivaItemKey = tipoRetrospectivaItemKey;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

}
