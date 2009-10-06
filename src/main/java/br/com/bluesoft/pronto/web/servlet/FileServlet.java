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

package br.com.bluesoft.pronto.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;

import br.com.bluesoft.pronto.service.Config;

public class FileServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

		final String url = request.getRequestURI();
		final String path = url.substring(url.indexOf("file/"));
		final String file = path.replaceAll("(.*)/", "");
		final String ticketKey = path.replaceAll("(.*)(/)(.*)(/)(.*)", "$3");

		final File arquivo = new File(Config.getImagesFolder() + ticketKey + "/" + file);
		final FileInputStream fis = new FileInputStream(arquivo);
		final int numberBytes = fis.available();
		final byte bytes[] = new byte[numberBytes];
		fis.read(bytes);
		fis.close();

		String extensao = null;
		if (file.lastIndexOf('.') > 0) {
			extensao = file.substring(file.lastIndexOf('.') + 1, file.length());
		}

		String mime = null;
		if (extensao == null) {
			mime = "text/plain";
		} else if (extensao.equalsIgnoreCase("png")) {
			mime = "images/png";
		} else if (extensao.equalsIgnoreCase("jpg") || extensao.equalsIgnoreCase("jpeg")) {
			mime = "images/jpeg";
		} else if (extensao.equalsIgnoreCase("gif")) {
			mime = "images/gif";
		} else if (extensao.equalsIgnoreCase("pdf")) {
			mime = "application/pdf";
		} else if (extensao.equalsIgnoreCase("xls") || extensao.equalsIgnoreCase("xlsx")) {
			mime = "application/vnd.ms-excel";
		} else if (extensao.equalsIgnoreCase("csv")) {
			mime = "text/csv";
		} else if (extensao.equalsIgnoreCase("txt")) {
			mime = "text/plain";
		} else if (extensao.equalsIgnoreCase("doc") || extensao.equalsIgnoreCase("docx")) {
			mime = "application/ms-word";
		}

		response.addHeader("content-disposition", "attachment; filename=" + file);
		response.setContentType(mime);
		response.setContentLength(bytes.length);
		FileCopyUtils.copy(bytes, response.getOutputStream());

	}

}
