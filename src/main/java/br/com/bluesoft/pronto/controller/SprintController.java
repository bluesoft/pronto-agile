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
	public String listar(final Model model) {
		model.addAttribute("sprints", sessionFactory.getCurrentSession().createCriteria(Sprint.class).list());
		return VIEW_LISTAR;
	}

	@RequestMapping("/sprint/atual.action")
	public String atual(final int sprintKey) {

		final Transaction tx = sessionFactory.getCurrentSession().beginTransaction();

		sessionFactory.getCurrentSession().createQuery("update Sprint s set s.atual = false, s.fechado = false").executeUpdate();
		sessionFactory.getCurrentSession().flush();

		final Sprint sprint = (Sprint) sessionFactory.getCurrentSession().get(Sprint.class, sprintKey);
		sprint.setAtual(true);
		sessionFactory.getCurrentSession().update(sprint);
		sessionFactory.getCurrentSession().flush();
		tx.commit();

		return "redirect:listar.action";
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
	public String upload(final HttpServletRequest request, final int sprintKey) throws Exception {
		final Sprint sprint = (Sprint) sessionFactory.getCurrentSession().get(Sprint.class, sprintKey);
		final byte[] bytes = getImageBytes(request);
		sprint.setImagem(Hibernate.createBlob(bytes));
		sessionFactory.getCurrentSession().saveOrUpdate(sprint);
		sessionFactory.getCurrentSession().flush();
		return "redirect:editar.action?sprintKey=" + sprintKey;
	}

	@RequestMapping("/sprint/imagem.action")
	public String imagem(final HttpServletResponse response, final int sprintKey) throws Exception {
		final Sprint sprint = (Sprint) sessionFactory.getCurrentSession().get(Sprint.class, sprintKey);
		final Blob imagem = sprint.getImagem();
		if (imagem != null) {
			final InputStream is = imagem.getBinaryStream();
			final byte[] bytes = new byte[is.available()];
			is.read(bytes);
			response.getOutputStream().write(bytes);
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	private byte[] getImageBytes(final HttpServletRequest request) throws FileUploadException, IOException {
		final FileItemFactory factory = new DiskFileItemFactory();
		final ServletFileUpload upload = new ServletFileUpload(factory);
		byte bytes[] = null;
		final List<FileItem> items = upload.parseRequest(request);
		for (final FileItem fileItem : items) {
			final InputStream inputStream = fileItem.getInputStream();
			final int numberBytes = inputStream.available();
			bytes = new byte[numberBytes];
			inputStream.read(bytes);
		}
		return bytes;
	}

}
