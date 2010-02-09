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

package br.com.bluesoft.pronto.controller

import javax.servlet.http.HttpServletResponse

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import br.com.bluesoft.pronto.service.WikiFormatter

@Controller
class WikiController {

	@RequestMapping("/wiki/parse")
	public String parse(final HttpServletResponse response, final String data) throws Exception {
		response.getOutputStream().print(WikiFormatter.toHtml(data))
		return null
	}
}
