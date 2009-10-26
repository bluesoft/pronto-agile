package br.com.bluesoft.pronto.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

		try {

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

		} catch (FileNotFoundException e) {
			
		}
	}

}
