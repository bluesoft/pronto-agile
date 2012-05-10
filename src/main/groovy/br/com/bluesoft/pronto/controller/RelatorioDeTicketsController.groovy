package br.com.bluesoft.pronto.controller
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import br.com.bluesoft.pronto.dao.RelatorioDeTicketsDao;
import br.com.bluesoft.pronto.web.binding.DefaultBindingInitializer 

@Controller
@RequestMapping("/relatorios/tickets")
class RelatorioDeTicketsController {
	
	final static int MAXIMO_DE_GRUPOS = 12
	final static String VIEW = "/relatorios/quantidades/index.jsp"
	final static cores = ['AFD8F8','F6BD0F','8BBA00','FF8E46','008E8E','D64646','8E468E','588526','B3AA00','008ED6','9D080D','A186BE']
	
	@Autowired
	RelatorioDeTicketsDao relatorioDao
	
	@InitBinder
	public void initBinder(final WebDataBinder binder, final WebRequest webRequest) {
		def defaultBindingInitializer = new DefaultBindingInitializer()
		defaultBindingInitializer.initBinder binder, webRequest
	}
	
	@RequestMapping
	String index(Model model){
		model.addAttribute 'dataInicial', new Date() - 31
		model.addAttribute 'dataFinal', new Date()
		return VIEW
	}
	
	@RequestMapping("/gerar.xml")
	void gerar(Date dataInicial, Date dataFinal, String tipoDeRelatorio, Integer tipoDeTicketKey, String referencia, String valor, HttpServletResponse response){
		
		def dados = null
		switch (tipoDeRelatorio) {
			case "sprint":
				dados = relatorioDao.listarPorSprint(dataInicial,dataFinal,tipoDeTicketKey,referencia,valor)
				dados = getJSON(sintetizar(dados), valor)
				break;
			case "release":
				dados = relatorioDao.listarPorRelease(dataInicial,dataFinal,tipoDeTicketKey,referencia,valor)
				dados = getJSON(sintetizar(dados), valor)
				break;
			case "categoria":
				dados = relatorioDao.listarPorCategoria(dataInicial,dataFinal,tipoDeTicketKey,referencia,valor)
				dados = getJSON(sintetizar(dados), valor)
				break;
			case "modulo":
				dados = relatorioDao.listarPorModulo(dataInicial,dataFinal,tipoDeTicketKey,referencia,valor)
				dados = getJSON(sintetizar(dados), valor)
				break;
			case "semana":
				dados = relatorioDao.listarPorSemana(dataInicial,dataFinal,tipoDeTicketKey,referencia,valor)
				dados = getJSON(dados, valor)
				break;
			case "mes":
				dados = relatorioDao.listarPorMes(dataInicial,dataFinal,tipoDeTicketKey,referencia,valor)
				dados = getJSON(dados, valor)
				break;
			case "ano":
				dados = relatorioDao.listarPorAno(dataInicial,dataFinal,tipoDeTicketKey,referencia,valor)
				dados = getJSON(dados, valor)
				break;
			case "cliente":
				dados = relatorioDao.listarPorCliente(dataInicial,dataFinal,tipoDeTicketKey,referencia,valor)
				dados = getJSON(sintetizar(dados), valor)
				break;
			case "esforco":
				dados = relatorioDao.listarPorEsforco(dataInicial,dataFinal,tipoDeTicketKey,referencia,valor)
				dados = getJSON(sintetizar(dados), valor)
				break;
			case "valor":
				dados = relatorioDao.listarPorValorDeNegocio(dataInicial,dataFinal,tipoDeTicketKey,referencia,valor)
				dados = getJSON(sintetizar(dados), valor)
				break;
		}
		
		response.setContentType("text/xml")
		response.writer.write dados
	}
	
	def getJSON(def defeitos, valor){
		def writer = new StringWriter()
		def builder = new groovy.xml.MarkupBuilder(writer)
		int showNameFactor = (defeitos.size() / 12) as Integer
		showNameFactor = showNameFactor == 0 ? 1 : showNameFactor
		int showValues = defeitos.size() > 30 ? 0 : 1
		builder.'graph'(yAxisName:"Demanda (${valor})",caption:'Tickets', rotateNames:'1',showValues:showValues) {
			defeitos.eachWithIndex { defeito, index  ->
				String color = defeitos.size() < 50  ? (index < 12 ? cores[index] : cores[index % 12]) : cores[0]
				int showName = index % showNameFactor == 0 ? 1 : 0
				'set'(name:defeito[0],value:defeito[1],color:color,showName:showName){}
			}
		}
		writer.toString()
	}
	
	def sintetizar(defeitos) {
		def numerosDeGrupos = defeitos.size()
		if (numerosDeGrupos > MAXIMO_DE_GRUPOS) {
			def sinteze = []
			def outros = 0
			defeitos.eachWithIndex { it, index ->
				if (index < MAXIMO_DE_GRUPOS) {
					sinteze[index] = defeitos[index]
				} else {
					outros += defeitos[index][1]
				}
			}
			sinteze[MAXIMO_DE_GRUPOS] = ['Outros', outros]
			return sinteze
		} else {
			return defeitos
		}
	}
}
