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

package br.com.bluesoft.pronto.web.binding;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;

import org.springframework.util.StringUtils;

public class SqlDateEditor extends java.beans.PropertyEditorSupport {

	private boolean allowEmpty = false;
	private final DateFormat dateFormat;

	public SqlDateEditor(final DateFormat dateFormat, final boolean allowEmpty) {
		this.allowEmpty = allowEmpty;
		this.dateFormat = dateFormat;
	}

	@Override
	public void setAsText(final String text) throws IllegalArgumentException {
		java.util.Date d = null;
		if (this.allowEmpty && !StringUtils.hasText(text)) {
			setValue(null);
		} else {
			try {
				d = dateFormat.parse(text);
				setValue(new Date(d.getTime()));
			} catch (final ParseException ex) {
				throw new IllegalArgumentException("Could not parse date: " + ex.getMessage());
			}
		}
	}

	@Override
	public String getAsText() {
		final Date value = (java.sql.Date) getValue();
		if (value != null) {
			final java.util.Date d = new java.util.Date(value.getTime());
			return dateFormat.format(d);

		} else {
			return "";
		}
	}
}
