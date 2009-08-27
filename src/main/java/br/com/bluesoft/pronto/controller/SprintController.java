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

package br.com.bluesoft.pronto.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.bluesoft.pronto.ProntoException;
import br.com.bluesoft.pronto.dao.SprintDao;
import br.com.bluesoft.pronto.dao.TicketDao;
import br.com.bluesoft.pronto.model.Sprint;
import br.com.bluesoft.pronto.service.Config;

@Controller
public class SprintController {

	private static final String VIEW_LISTAR = "/sprint/sprint.listar.jsp";
	private static final String VIEW_EDITAR = "/sprint/sprint.editar.jsp";

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private SprintDao sprintDao;

	@Autowired
	private TicketDao ticketDao;

	@RequestMapping("/sprint/listar.action")
	public String listar(final Model model) {
		model.addAttribute("sprints", sprintDao.listar());
		return VIEW_LISTAR;
	}

	@RequestMapping("/sprint/atual.action")
	public String atual(final int sprintKey) {

		final Transaction tx = sessionFactory.getCurrentSession().beginTransaction();

		sessionFactory.getCurrentSession().createQuery("update Sprint s set s.atual = false").executeUpdate();
		sessionFactory.getCurrentSession().flush();

		final Sprint sprint = (Sprint) sessionFactory.getCurrentSession().get(Sprint.class, sprintKey);
		sprint.setAtual(true);
		sprint.setFechado(false);
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

		if (sprint.getDataFinal().before(sprint.getDataInicial())) {
			model.addAttribute("sprintKey", sprint.getSprintKey());
			model.addAttribute("erro", "A data inicial deve ser menor que a final");
			return "forward:editar.action";
		}

		final Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		sessionFactory.getCurrentSession().saveOrUpdate(sprint);
		sessionFactory.getCurrentSession().flush();
		tx.commit();
		return "forward:listar.action";
	}

	@RequestMapping("/sprint/upload.action")
	public String upload(final HttpServletRequest request, final int sprintKey) throws Exception {
		final Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
		final Sprint sprint = (Sprint) sessionFactory.getCurrentSession().get(Sprint.class, sprintKey);

		final byte[] bytes = getImageBytes(request);

		final String folderPath = Config.getImagesFolder() + "/sprints/";
		final File folder = new File(folderPath);
		folder.mkdirs();

		final File file = new File(folderPath + sprint.getSprintKey());
		final FileOutputStream outputStream = new FileOutputStream(file);
		outputStream.write(bytes);
		outputStream.flush();

		sessionFactory.getCurrentSession().saveOrUpdate(sprint);
		sessionFactory.getCurrentSession().flush();

		tx.commit();
		return "redirect:editar.action?sprintKey=" + sprintKey;
	}

	@RequestMapping("/sprint/imagem.action")
	public String imagem(final HttpServletResponse response, final int sprintKey) throws Exception {

		final String folderPath = Config.getImagesFolder() + "/sprints/";

		final File arquivo = new File(folderPath + sprintKey);

		final byte[] bytes;
		if (arquivo.exists()) {
			final FileInputStream fis = new FileInputStream(arquivo);
			final int numberBytes = fis.available();
			bytes = new byte[numberBytes];
			fis.read(bytes);
			fis.close();
		} else {
			final InputStream resource = this.getClass().getResourceAsStream("/noImage.jpg");
			final int numberBytes = resource.available();
			bytes = new byte[numberBytes];
			resource.read(bytes);
			resource.close();
		}

		response.getOutputStream().write(bytes);
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

	@RequestMapping("/sprint/fechar.action")
	public String fechar(final Model model, final int sprintKey) throws ProntoException {

		try {
			final Sprint sprintParaFechar = (Sprint) sessionFactory.getCurrentSession().get(Sprint.class, sprintKey);

			if (sprintParaFechar.isAtual()) {
				throw new ProntoException("Não é possível fechar o Sprint Atual!");
			}

			sprintDao.fecharSprint(sprintParaFechar);

			return "redirect:/sprint/listar.action";
		} catch (final Exception e) {
			model.addAttribute("erro", e.getMessage());
			return "forward:/sprint/listar.action";
		}

	}

	@RequestMapping("/sprint/reabrir.action")
	public String reabrir(final Model model, final int sprintKey) throws ProntoException {
		try {
			final Sprint sprintParaReabrir = (Sprint) sessionFactory.getCurrentSession().get(Sprint.class, sprintKey);

			final Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
			sprintParaReabrir.setFechado(false);
			sprintDao.salvar(sprintParaReabrir);
			tx.commit();

			return "redirect:/sprint/listar.action";
		} catch (final Exception e) {
			model.addAttribute("erro", e.getMessage());
			return "forward:/sprint/listar.action";
		}
	}

}
