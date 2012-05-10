package br.com.bluesoft.pronto.controller



import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod


import br.com.bluesoft.pronto.core.Papel
import br.com.bluesoft.pronto.dao.TicketDao
import br.com.bluesoft.pronto.service.Seguranca
import static org.springframework.web.bind.annotation.RequestMethod.*

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
	
	private static final String VIEW_DASHBOARD = "/dashboard/dashboard.dashboard.jsp"
	
	@Autowired TicketDao ticketDao
	
	@RequestMapping(value= '/', method = RequestMethod.GET)
	String index(final Model model) {
		
		Seguranca.validarPermissao Papel.PRODUCT_OWNER, Papel.EQUIPE, Papel.SCRUM_MASTER
		
		def itens = ticketDao.listarItensParaDashboard()
		model.addAttribute "itens", itens
		
		VIEW_DASHBOARD
	}
	
	
	
}
