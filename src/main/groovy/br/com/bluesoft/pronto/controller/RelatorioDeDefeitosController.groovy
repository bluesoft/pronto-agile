package br.com.bluesoft.pronto.controller
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import br.com.bluesoft.pronto.dao.RelatorioDeDefeitosDao;
import br.com.bluesoft.pronto.web.binding.DefaultBindingInitializer 

@Controller
@RequestMapping("/relatorios/defeitos")
class RelatorioDeDefeitosController {
	
	final static String VIEW = "/relatorios/defeitos/index.jsp"
	final static int MAXIMO_DE_GRUPOS = 10
		
	@Autowired
	RelatorioDeDefeitosDao relatorioDeDefeitosDao
	
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
	void gerar(Date dataInicial, Date dataFinal, String tipo, HttpServletResponse response){
		
		def dados = null
		switch(tipo){
			case "sprint":
				dados = relatorioDeDefeitosDao.listarDefeitosPorSprint(dataInicial,dataFinal)
				break;
			case "categoria":
				dados = relatorioDeDefeitosDao.listarDefeitosPorCategoria(dataInicial,dataFinal)
				break;
			case "modulo":
				dados = relatorioDeDefeitosDao.listarDefeitosPorModulo(dataInicial,dataFinal)
				break;
			default:
				dados = relatorioDeDefeitosDao.listarDefeitosPorCliente(dataInicial,dataFinal)
				break;
		}
		
		response.setContentType("text/xml")
		response.writer.write getJSON(sintetizar(dados))
	}
	
	def getJSON(def defeitos){
		def writer = new StringWriter()
		def builder = new groovy.xml.MarkupBuilder(writer)
		builder.'graph'(yAxisName:'Quantidade de Defeitos',caption:'Defeitos') {
			defeitos.each { defeito ->
				'set'(name:defeito[0],value:defeito[1]){}
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
