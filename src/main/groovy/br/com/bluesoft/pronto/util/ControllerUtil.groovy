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

import javax.servlet.http.HttpServletResponse;

public class ControllerUtil {

	public static final String ENCODING_UTF8 = "UTF-8";

	public static void writeText(final HttpServletResponse response, final Object text) {
		try {
			response.setContentType("text/plain");
			response.setCharacterEncoding(ENCODING_UTF8);
			response.getWriter().print(text);
		} catch (final Exception e) {
			throw new RuntimeException("Não foi possível gerar a resposta JSON.", e);
		}
	}

}
