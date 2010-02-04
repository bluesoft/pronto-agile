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

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.SequenceGenerator

import br.com.bluesoft.pronto.core.TipoRetrospectivaItem

@Entity
@SequenceGenerator(name = "SEQ_RETROSPECTIVA_ITEM", sequenceName = "SEQ_RETROSPECTIVA_ITEM")
class RetrospectivaItem implements Comparable<RetrospectivaItem> {
	
	@Id
	@GeneratedValue(generator = "SEQ_RETROSPECTIVA_ITEM")
	int retrospectivaItemKey
	
	@ManyToOne
	@JoinColumn(name = "RETROSPECTIVA_KEY")
	Retrospectiva retrospectiva
	
	String descricao
	
	@ManyToOne
	@JoinColumn(name = "TIPO_RETROSPECTIVA_ITEM_KEY")
	TipoRetrospectivaItem tipoRetrospectivaItem
	
	@Override
	int compareTo(final RetrospectivaItem other) {
		return Integer.valueOf(this.retrospectivaItemKey).compareTo(Integer.valueOf(other.getRetrospectivaItemKey()))
	}
	
}
