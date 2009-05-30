package br.com.bluesoft.pronto.controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.model.Sprint;

@Controller
public class SprintController {

	private static final String VIEW_LISTAR = "/sprint/sprint.listar.jsp";
	private static final String VIEW_EDITAR = "/sprint/sprint.editar.jsp";

	@Autowired
	private SessionFactory sessionFactory;

	@RequestMapping("/sprint/listar.action")
	public String listar(Model model) {
		model.addAttribute("sprints", sessionFactory.getCurrentSession().createCriteria(Sprint.class).list());
		return VIEW_LISTAR;
	}

	@RequestMapping("/sprint/editar.action")
	public String editar(final Model model, final Integer sprintKey) {

		if (sprintKey != null) {
			final Sprint sprint = (Sprint) sessionFactory.getCurrentSession().get(Sprint.class, sprintKey);
			model.addAttribute("sprint", sprint);
		} else {
			model.addAttribute("sprint", new Sprint());
		}

		return VIEW_EDITAR;
	}

	@RequestMapping("/sprint/salvar.action")
	public String salvar(final Model model, final Sprint sprint) {
		final Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		sessionFactory.getCurrentSession().saveOrUpdate(sprint);
		sessionFactory.getCurrentSession().flush();
		tx.commit();
		return "forward:listar.action";
	}

	@RequestMapping("/sprint/upload.action")
	public String upload(HttpServletRequest request, int sprintKey) throws Exception {
		final Sprint sprint = (Sprint) sessionFactory.getCurrentSession().get(Sprint.class, sprintKey);
		byte[] bytes = getImageBytes(request);
		sprint.setImagem(Hibernate.createBlob(bytes));
		sessionFactory.getCurrentSession().saveOrUpdate(sprint);
		sessionFactory.getCurrentSession().flush();
		return "redirect:editar.action?sprintKey=" + sprintKey;
	}

	@RequestMapping("/sprint/imagem.action")
	public String imagem(HttpServletResponse response, int sprintKey) throws Exception {
		final Sprint sprint = (Sprint) sessionFactory.getCurrentSession().get(Sprint.class, sprintKey);
		Blob imagem = sprint.getImagem();
		if (imagem != null) {
			InputStream is = imagem.getBinaryStream();
			byte[] bytes = new byte[is.available()];
			is.read(bytes);
			response.getOutputStream().write(bytes);
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	private byte[] getImageBytes(HttpServletRequest request) throws FileUploadException, IOException {
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		byte bytes[] = null;
		List<FileItem> items = upload.parseRequest(request);
		for (FileItem fileItem : items) {
			InputStream inputStream = fileItem.getInputStream();
			int numberBytes = inputStream.available();
			bytes = new byte[numberBytes];
			inputStream.read(bytes);
		}
		return bytes;
	}

}
