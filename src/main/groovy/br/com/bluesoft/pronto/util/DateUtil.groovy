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

package br.com.bluesoft.pronto.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	private static final String PADRAO_COMPLETO = "dd/MM/yyyy";
	private static final String PADRAO_MES_ANO = "dd/MM";
	private static final String PADRAO_HORA = "HH:mm";

	public static String toString(final Date date) {
		if (date != null) {
			return new SimpleDateFormat(PADRAO_COMPLETO).format(date);
		} else {
			return null;
		}
	}

	public static String toStringMesAno(final Date date) {
		return new SimpleDateFormat(PADRAO_MES_ANO).format(date);
	}

	public static String toStringHora(final Date date) {
		return new SimpleDateFormat(PADRAO_HORA).format(date);
	}

	public static Date add(final Date date, final int days) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, days);
		return calendar.getTime();
	}

}
