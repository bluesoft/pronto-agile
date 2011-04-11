package br.com.bluesoft.pronto.controller;

import java.util.Date

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.context.request.WebRequest

import br.com.bluesoft.pronto.dao.TicketDao
import br.com.bluesoft.pronto.to.ReleaseNote
import br.com.bluesoft.pronto.util.DateUtil;
import br.com.bluesoft.pronto.web.binding.DefaultBindingInitializer

@Controller
public class ReleaseNotesController {

	@Autowired
	private TicketDao ticketDao

	@RequestMapping("/relatorios/releaseNotes")
	String index(Model model, Date dataInicial, Date dataFinal) {
		model.addAttribute 'dataInicial', new Date() - 31
		model.addAttribute 'dataFinal', new Date()
		return "/relatorios/releaseNotes/index.jsp"
	}

	@ResponseBody
	@RequestMapping("/relatorios/releaseNotes/listar")
	List<ReleaseNote> listar(Model model, String dataInicial, String dataFinal) {
		return ticketDao.listarNotasDeRelease(DateUtil.toDate(dataInicial), DateUtil.toDate(dataFinal))
	}
}
