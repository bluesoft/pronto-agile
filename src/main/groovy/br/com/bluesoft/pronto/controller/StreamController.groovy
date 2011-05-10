package br.com.bluesoft.pronto.controller

import java.util.Date
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

import com.google.common.collect.Maps;

import br.com.bluesoft.pronto.ProntoException;
import br.com.bluesoft.pronto.SegurancaException
import br.com.bluesoft.pronto.core.KanbanStatus
import br.com.bluesoft.pronto.core.Papel
import br.com.bluesoft.pronto.dao.KanbanStatusDao
import br.com.bluesoft.pronto.dao.MotivoReprovacaoDao
import br.com.bluesoft.pronto.dao.MovimentoKanbanDao
import br.com.bluesoft.pronto.dao.SprintDao
import br.com.bluesoft.pronto.dao.TicketDao
import br.com.bluesoft.pronto.model.MovimentoKanban;
import br.com.bluesoft.pronto.model.Sprint
import br.com.bluesoft.pronto.model.Ticket
import br.com.bluesoft.pronto.service.Seguranca
import br.com.bluesoft.pronto.service.MovimentadorDeTicket;
import br.com.bluesoft.pronto.service.StreamService;
import static org.springframework.web.bind.annotation.RequestMethod.*

@Controller
@RequestMapping("/stream")
public class StreamController {
	
	private static final String VIEW_STREAM = "/stream/stream.stream.jsp"
	
	@Autowired StreamService streamService
	
	@RequestMapping(value= '/', method = GET)
	String index(final Model model) {
		
		Seguranca.validarPermissao Papel.PRODUCT_OWNER, Papel.EQUIPE, Papel.SCRUM_MASTER
		
		model.addAttribute "stream", streamService.listarStream()
		
		VIEW_STREAM
	}
	
	
	
}
