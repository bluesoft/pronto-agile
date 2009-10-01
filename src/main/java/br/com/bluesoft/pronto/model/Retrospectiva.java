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

import java.util.List;
import java.util.SortedSet;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import br.com.bluesoft.pronto.core.TipoRetrospectivaItem;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Entity
@SequenceGenerator(name = "SEQ_RETROSPECTIVA", sequenceName = "SEQ_RETROSPECTIVA")
public class Retrospectiva {

	@Id
	@GeneratedValue(generator = "SEQ_RETROSPECTIVA")
	private int retrospectivaKey;

	@ManyToOne
	@JoinColumn(name = "SPRINT_KEY")
	private Sprint sprint;

	@Sort(type = SortType.NATURAL)
	@OneToMany(mappedBy = "retrospectiva")
	private SortedSet<RetrospectivaItem> itens;

	public int getRetrospectivaKey() {
		return retrospectivaKey;
	}

	public void setRetrospectivaKey(final int retrospectivaKey) {
		this.retrospectivaKey = retrospectivaKey;
	}

	public Sprint getSprint() {
		return sprint;
	}

	public void setSprint(final Sprint sprint) {
		this.sprint = sprint;
	}

	public RetrospectivaItem getItemPorKey(final int retrospectivaItemKey) {
		return Iterables.<RetrospectivaItem> find(itens, new Predicate<RetrospectivaItem>() {

			@Override
			public boolean apply(final RetrospectivaItem i) {
				return i.getRetrospectivaItemKey() == retrospectivaItemKey;
			}
		});
	}

	public List<RetrospectivaItem> getWww() {
		if (itens != null) {
			return Lists.newLinkedList(Iterables.<RetrospectivaItem> filter(itens, new Predicate<RetrospectivaItem>() {

				@Override
				public boolean apply(final RetrospectivaItem i) {
					return i.getTipoRetrospectivaItem().getTipoRetrospectivaItemKey() == TipoRetrospectivaItem.WWW;
				}
			}));
		} else {
			return null;
		}
	}

	public List<RetrospectivaItem> getWcbi() {
		if (itens != null) {
			return Lists.newLinkedList(Iterables.<RetrospectivaItem> filter(itens, new Predicate<RetrospectivaItem>() {

				@Override
				public boolean apply(final RetrospectivaItem i) {
					return i.getTipoRetrospectivaItem().getTipoRetrospectivaItemKey() == TipoRetrospectivaItem.WCBI;
				}
			}));
		} else {
			return null;
		}
	}

}
