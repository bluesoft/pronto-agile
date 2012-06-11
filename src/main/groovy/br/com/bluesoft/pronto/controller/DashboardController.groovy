package br.com.bluesoft.pronto.controller

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

import br.com.bluesoft.pronto.core.Papel
import br.com.bluesoft.pronto.dao.DashboardDao
import br.com.bluesoft.pronto.dao.TicketDao
import br.com.bluesoft.pronto.service.Seguranca
import static org.springframework.web.bind.annotation.RequestMethod.*

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
	
	private static final String VIEW_DASHBOARD = "/dashboard/dashboard.dashboard.jsp"
	
	@Autowired TicketDao ticketDao
	@Autowired DashboardDao dashboardDao
	
	@RequestMapping(value= '/', method = RequestMethod.GET)
	String index(final Model model) {
		
		Seguranca.validarPermissao Papel.PRODUCT_OWNER, Papel.EQUIPE, Papel.SCRUM_MASTER
		
		def itens = ticketDao.listarItensParaDashboard()
		model.addAttribute "itens", itens
		model.addAttribute 'defeitos', toGoogleChartsArray(dashboardDao.obterDefeitosParaCockpit())
		model.addAttribute 'defeitosEntregues', toGoogleChartsArray(dashboardDao.obterDefeitosEntreguesParaCockpit())
		model.addAttribute 'entregas', toGoogleChartsArray(dashboardDao.obterEntregasParaCockpit())
		
		VIEW_DASHBOARD
	}
	
	def toGoogleChartsArray(map) {
		JSONArray array = new JSONArray(); 
		array.add(JSONArray.fromObject(['Label','Value']));
		map.each { 
			array.add(JSONArray.fromObject([it.key, it .value]));
		}
		return array
	}
	
}
