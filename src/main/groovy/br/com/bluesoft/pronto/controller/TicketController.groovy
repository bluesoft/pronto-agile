package br.com.bluesoft.pronto.controller

import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException;
import java.util.ArrayList
import java.util.Arrays
import java.util.Collections
import java.util.HashMap
import java.util.List
import java.util.Map
import java.util.Set
import java.util.TreeSet

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.apache.commons.fileupload.FileItem
import org.apache.commons.fileupload.FileItemFactory
import org.apache.commons.fileupload.disk.DiskFileItemFactory
import org.apache.commons.fileupload.servlet.ServletFileUpload
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.criterion.Restrictions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.FileCopyUtils
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import br.com.bluesoft.pronto.ProntoException
import br.com.bluesoft.pronto.SegurancaException
import br.com.bluesoft.pronto.core.Backlog
import br.com.bluesoft.pronto.core.Papel
import br.com.bluesoft.pronto.core.TipoDeTicket
import br.com.bluesoft.pronto.dao.BacklogDao
import br.com.bluesoft.pronto.dao.CategoriaDao;
import br.com.bluesoft.pronto.dao.ClienteDao
import br.com.bluesoft.pronto.dao.KanbanStatusDao
import br.com.bluesoft.pronto.dao.SprintDao
import br.com.bluesoft.pronto.dao.TicketDao
import br.com.bluesoft.pronto.dao.TipoDeTicketDao
import br.com.bluesoft.pronto.dao.ConfiguracaoDao
import br.com.bluesoft.pronto.dao.UsuarioDao
import br.com.bluesoft.pronto.model.Anexo
import br.com.bluesoft.pronto.model.Classificacao
import br.com.bluesoft.pronto.model.Sprint
import br.com.bluesoft.pronto.model.Ticket
import br.com.bluesoft.pronto.model.TicketLog
import br.com.bluesoft.pronto.model.TicketOrdem
import br.com.bluesoft.pronto.model.Usuario
import br.com.bluesoft.pronto.service.Config
import br.com.bluesoft.pronto.service.Seguranca
import br.com.bluesoft.pronto.util.ControllerUtil
import br.com.bluesoft.pronto.util.DateUtil
import br.com.bluesoft.pronto.util.StringUtil
import br.com.bluesoft.pronto.web.binding.DefaultBindingInitializer;

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap

import static org.springframework.web.bind.annotation.RequestMethod.*

@Controller
@RequestMapping("/tickets")
class TicketController {
	
	public static final String VIEW_LISTAR = "/ticket/ticket.listar.jsp"
	public static final String VIEW_LISTAR_AGRUPADO = "/ticket/ticket.listarAgrupado.jsp"
	public static final String VIEW_BRANCHES = "/ticket/ticket.branches.jsp"
	public static final String VIEW_ESTIMAR = "/ticket/ticket.estimar.jsp"
	public static final String VIEW_EDITAR = "/ticket/ticket.editar.jsp"

	@Autowired CategoriaDao categoriaDao
	@Autowired ClienteDao clienteDao
	@Autowired SessionFactory sessionFactory
	@Autowired TicketDao ticketDao
	@Autowired SprintDao sprintDao
	@Autowired UsuarioDao usuarioDao
	@Autowired KanbanStatusDao kanbanStatusDao
	@Autowired TipoDeTicketDao tipoDeTicketDao
	@Autowired BacklogDao backlogDao
	@Autowired ConfiguracaoDao configuracaoDao
	
	@InitBinder
	public void initBinder(final WebDataBinder binder, final WebRequest webRequest) {
		def defaultBindingInitializer = new DefaultBindingInitializer()
		defaultBindingInitializer.initBinder binder, webRequest
	}
	
	@ModelAttribute("usuarios")
	List<Usuario> getUsuarios() {
		return usuarioDao.listar()
	}
	
	@ModelAttribute("tiposDeTicket")
	List<TipoDeTicket> getTiposDeTicket() {
		return tipoDeTicketDao.listar()
	}
	
	@RequestMapping("/branches")
	String branches( Model model) {
		List<Ticket> tickets = ticketDao.listarTicketsQueNaoEstaoNoBranchMaster()
		model.addAttribute("tickets", tickets)
		return VIEW_BRANCHES
	}
	
	@RequestMapping(value='/{ticketKey}/comentarios', method=[POST, PUT])
	String incluirComentario(@PathVariable int ticketKey, String comentario){
		
		Seguranca.validarPermissao Papel.PRODUCT_OWNER, Papel.EQUIPE, Papel.SCRUM_MASTER
		
		Transaction tx = sessionFactory.getCurrentSession().beginTransaction()
		def ticket = ticketDao.obter(ticketKey)
		ticket.addComentario comentario, Seguranca.usuario
		ticketDao.salvar ticket
		tx.commit()
		
		return "redirect:/tickets/${ticketKey}#comentarios"
		
	}

	@RequestMapping(method=[POST, PUT])
	String salvar( Model model, Ticket ticket,  String comentario,  String[] desenvolvedor,  String[] testador,  Integer paiKey,  Integer clienteKey) throws SegurancaException {
		
		Seguranca.validarPermissao Papel.PRODUCT_OWNER, Papel.EQUIPE, Papel.SCRUM_MASTER
		
		try {
			
			if (ticket.ticketKey > 0) {
				def dataDaUltimaAlteracao = DateUtil.getTimestampSemMilissegundos(ticketDao.obterDataDaUltimaAlteracaoDoTicket(ticket.ticketKey))
				if (dataDaUltimaAlteracao!= null && ticket.dataDaUltimaAlteracao < dataDaUltimaAlteracao) {
					def erro = 'Não foi possivel alterar o Ticket porque ele já foi alterado depois que você começou a editá-lo!' 
					return "redirect:/tickets/${ticket.ticketKey}?erro=${erro}";
				}
			}

			Transaction tx = sessionFactory.getCurrentSession().beginTransaction()
			
			if (ticket.getTitulo() == null || ticket.getTitulo().trim().length() <= 0) {
				throw new ProntoException("Não é possível salvar uma estória, defeito ou tarefa sem descrição!")
			}
			
			if (paiKey == null) {
				
				ticket.setBacklog(backlogDao.obter(ticket.getBacklog().getBacklogKey()))
				ticket.setTipoDeTicket(tipoDeTicketDao.obter(ticket.getTipoDeTicket().getTipoDeTicketKey()))
				
				if (ticket.getSprint() != null && ticket.getSprint().getSprintKey() <= 0) {
					ticket.setSprint(null)
				} else {
					ticket.setSprint((Sprint) sessionFactory.getCurrentSession().get(Sprint.class, ticket.getSprint().getSprintKey()))
				}
				
			} else {
				Ticket pai = ticketDao.obter(paiKey)
				copiarDadosDoPai(pai, ticket)
			}
			
			ticket.setKanbanStatus(kanbanStatusDao.obter(ticket.getKanbanStatus().getKanbanStatusKey()))
			
			if (clienteKey != null) {
				ticket.setCliente(clienteDao.obter(clienteKey))
			} 
			
			if (ticket.categoria != null) {
				ticket.setCategoria(categoriaDao.obter(ticket.categoria.categoriaKey))
			}
			
			if (ticket.getTicketKey() == 0) {
				ticket.setReporter(usuarioDao.obter(ticket.getReporter().getUsername()))
			}
			
			if (comentario != null && comentario.trim().length() > 0) {
				ticket.addComentario(comentario, Seguranca.getUsuario())
			}
			
			definirDesenvolvedores(ticket, desenvolvedor)
			definirTestadores(ticket, testador)
			
			ticketDao.salvar(ticket)
			
			tx.commit()
			
			return "redirect:/tickets/${ticket.ticketKey}"
		} catch ( Exception e) {
			e.printStackTrace()
			return "redirect:/tickets/${ticket.ticketKey}?erro=${e.message}"
		}
	}
	
	void definirDesenvolvedores( Ticket ticket,  String[] desenvolvedor) throws SegurancaException {
		
		Set<Usuario> desenvolvedoresAntigos = new TreeSet<Usuario>(ticketDao.listarDesenvolvedoresDoTicket(ticket.getTicketKey()))
		
		if (desenvolvedor != null && desenvolvedor.length > 0) {
			ticket.setDesenvolvedores(new TreeSet<Usuario>())
			for ( String username : desenvolvedor) {
				ticket.addDesenvolvedor((Usuario) sessionFactory.getCurrentSession().get(Usuario.class, username))
			}
		}
		
		String desenvolvedoresAntigosStr = desenvolvedoresAntigos == null || desenvolvedoresAntigos.size() == 0 ? "nenhum" : desenvolvedoresAntigos.toString()
		String desenvolvedoresNovosStr = ticket.getDesenvolvedores() == null || ticket.getDesenvolvedores().size() == 0 ? "nenhum" : ticket.getDesenvolvedores().toString()
		if (!desenvolvedoresAntigosStr.equals(desenvolvedoresNovosStr)) {
			ticket.addLogDeAlteracao("desenvolvedores", desenvolvedoresAntigosStr, desenvolvedoresNovosStr)
		}
	}
	
	void definirTestadores( Ticket ticket,  String[] testador) throws SegurancaException {
		
		Set<Usuario> testadoresAntigos = new TreeSet<Usuario>(ticketDao.listarTestadoresDoTicket(ticket.getTicketKey()))
		
		if (testador != null && testador.length > 0) {
			ticket.setTestadores(new TreeSet<Usuario>())
			for ( String username : testador) {
				ticket.addTestador(usuarioDao.obter(username))
			}
		}
		
		String testadoresAntigosStr = testadoresAntigos == null || testadoresAntigos.size() == 0 ? "nenhum" : testadoresAntigos.toString()
		String testadoresNovosStr = ticket.getTestadores() == null || ticket.getTestadores().size() == 0 ? "nenhum" : ticket.getTestadores().toString()
		if (!testadoresAntigosStr.equals(testadoresNovosStr)) {
			ticket.addLogDeAlteracao("testadores", testadoresAntigosStr, testadoresNovosStr)
		}
	}
	
	@RequestMapping("/{ticketKey}/jogarNoLixo")
	String jogarNoLixo( Model model, @PathVariable int ticketKey,  HttpServletResponse response) throws SegurancaException {
		
		Seguranca.validarPermissao(Papel.PRODUCT_OWNER, Papel.EQUIPE)
		
		Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey)
		ticket.setBacklog((Backlog) sessionFactory.getCurrentSession().get(Backlog.class, Backlog.LIXEIRA))
		ticketDao.salvar(ticket)
		return "redirect:/tickets/${ticketKey}"
	}
	
	@RequestMapping("/{ticketKey}/moverParaImpedimentos")
	String moverParaImpedimentos( Model model, @PathVariable  int ticketKey,  HttpServletResponse response) throws SegurancaException {
		
		Seguranca.validarPermissao(Papel.PRODUCT_OWNER, Papel.EQUIPE)
		
		Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey)
		ticket.setBacklog((Backlog) sessionFactory.getCurrentSession().get(Backlog.class, Backlog.IMPEDIMENTOS))
		ticketDao.salvar(ticket)
		return "redirect:/tickets/${ticketKey}"
	}
	
	@RequestMapping("/{ticketKey}/moverParaBranchMaster")
	void moverParaBranchMaster( Model model,  @PathVariable int ticketKey,  HttpServletResponse response) throws SegurancaException {
		
		Seguranca.validarPermissao(Papel.PRODUCT_OWNER, Papel.EQUIPE)
		
		Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey)
		ticket.setBranch(Ticket.BRANCH_MASTER)
		ticketDao.salvar(ticket)
		
	}
	
	@RequestMapping("/{ticketKey}/moverParaProductBacklog")
	String moverParaProductBacklog( Model model,  @PathVariable int ticketKey,  HttpServletResponse response) throws SegurancaException {
		
		Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey)
		
		int backlogDeOrigem = ticket.getBacklog().getBacklogKey()
		if (backlogDeOrigem == Backlog.IDEIAS) {
			Seguranca.validarPermissao(Papel.PRODUCT_OWNER)
		}
		
		ticket.setBacklog((Backlog) sessionFactory.getCurrentSession().get(Backlog.class, Backlog.PRODUCT_BACKLOG))
		
		// caso não seja uma ideia mantém o tipo original
		if (ticket.getTipoDeTicket().getTipoDeTicketKey() == TipoDeTicket.IDEIA) {
			ticket.setTipoDeTicket((TipoDeTicket) sessionFactory.getCurrentSession().get(TipoDeTicket.class, TipoDeTicket.ESTORIA))
		}
		
		ticket.setSprint(null)
		ticketDao.salvar(ticket)
		return "redirect:/tickets/${ticketKey}"
	}
	
	@RequestMapping("/{ticketKey}/moverParaSprintAtual")
	String moverParaOSprintAtual( Model model,  @PathVariable int ticketKey,  HttpServletResponse response) throws ProntoException {
		
		Seguranca.validarPermissao(Papel.PRODUCT_OWNER)
		
		Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey)
		
		int backlogDeOrigem = ticket.getBacklog().getBacklogKey()
		if (backlogDeOrigem != Backlog.PRODUCT_BACKLOG) {
			throw new ProntoException("Para que uma Estória ou Bug seja movida para o Sprint Atual é preciso que ela esteja no Product Backlog.")
		}
		
		ticket.setSprint(sprintDao.getSprintAtual())
		ticket.setBacklog(backlogDao.obter(Backlog.SPRINT_BACKLOG))
		ticketDao.salvar(ticket)
		return "redirect:/tickets/${ticket.ticketKey}"
		
	}
	
	@RequestMapping("/{ticketKey}/moverParaIdeias")
	String moverParaIdeias( Model model, @PathVariable int ticketKey,  HttpServletResponse response) throws SegurancaException {
		
		Seguranca.validarPermissao(Papel.PRODUCT_OWNER)
		
		Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey)
		ticket.setBacklog((Backlog) sessionFactory.getCurrentSession().get(Backlog.class, Backlog.IDEIAS))
		ticket.setTipoDeTicket((TipoDeTicket) sessionFactory.getCurrentSession().get(TipoDeTicket.class, TipoDeTicket.IDEIA))
		ticket.setSprint(null)
		ticketDao.salvar(ticket)
		
		return "redirect:/tickets/${ticket.ticketKey}"
	}
	
	@RequestMapping("/{ticketKey}/restaurar")
	String restaurar( Model model, @PathVariable  int ticketKey,  HttpServletResponse response) throws SegurancaException {
		
		Seguranca.validarPermissao(Papel.PRODUCT_OWNER, Papel.EQUIPE)
		
		Ticket ticket = (Ticket) sessionFactory.getCurrentSession().get(Ticket.class, ticketKey)
		
		Backlog backlog = null
		switch (ticket.getTipoDeTicket().getTipoDeTicketKey()) {
			case TipoDeTicket.ESTORIA:
			case TipoDeTicket.DEFEITO:
				backlog = (Backlog) sessionFactory.getCurrentSession().get(Backlog.class, Backlog.PRODUCT_BACKLOG)
				break
			case TipoDeTicket.IDEIA:
				backlog = (Backlog) sessionFactory.getCurrentSession().get(Backlog.class, Backlog.IDEIAS)
				break
			case TipoDeTicket.TAREFA:
				backlog = ticket.getPai().getBacklog()
				break
		}
		
		ticket.setBacklog(backlog)
		
		if (!ticket.isTarefa() && ticket.getSprint() != null && ticket.getSprint().isFechado()) {
			ticket.setSprint(null)
		}
		
		ticketDao.salvar(ticket)
		
		if (ticket.getFilhos() != null) {
			for ( Ticket filho : ticket.getFilhos()) {
				filho.setBacklog(backlog)
				ticketDao.salvar(ticket)
			}
		}
		
		return "redirect:/tickets/${ticketKey}"
	}
	
	@RequestMapping("/{ticketKey}/upload")
	String upload( Model model,  HttpServletRequest request, @PathVariable int ticketKey) throws Exception {
		
		FileItemFactory factory = new DiskFileItemFactory()
		ServletFileUpload upload = new ServletFileUpload(factory)
		
		List<FileItem> items = upload.parseRequest(request)
		String ticketDir = Config.getImagesFolder() + ticketKey + "/"
		File dir = new File(ticketDir)
		dir.mkdirs()
		
		List<String> nomesDosArquivos = new ArrayList<String>()

		Ticket ticket = ticketDao.obter(ticketKey)

		for ( FileItem fileItem : items) {
			String nomeDoArquivo = StringUtil.retiraAcentuacao(fileItem.getName().toLowerCase().replace(' ', '_')).replaceAll("[^A-Za-z0-9._\\-]", "")
			
			if (ehUmNomeDeArquivoValido(nomeDoArquivo)) {
				fileItem.write(new File(ticketDir + nomeDoArquivo))
				
				nomesDosArquivos.add(nomeDoArquivo)
				ticket.addLogDeInclusao 'anexo', nomeDoArquivo
			} else {
				model.addAttribute("erro", "Não é possível anexar o arquivo '" + nomeDoArquivo + "' pois este não possui uma extensão.")
			}
		}
		
		this.insereImagensNaDescricao(ticketKey, nomesDosArquivos)

		ticketDao.salvar ticket

		return "redirect:/tickets/${ticketKey}"
	}
	
	boolean ehUmNomeDeArquivoValido( String nomeDoArquivo) {
		if (nomeDoArquivo.lastIndexOf('.') > 0) {
			return true
		}
		return false
	}
	
	void insereImagensNaDescricao( int ticketKey,  List<String> nomesDosArquivos) {
		Ticket ticket = ticketDao.obter(ticketKey)
		
		String novaDescricao = ticket.getDescricao()
		
		for ( String nomeDoArquivo : nomesDosArquivos) {
			if (ehImagem(getExtensao(nomeDoArquivo))) {
				novaDescricao += "\r\n\r\n[[Image:" + ticketKey + "/" + nomeDoArquivo + "]]"
			}
		}
		
		ticket.setDescricao(novaDescricao)
		ticketDao.salvar(ticket)
	}
	
	boolean ehImagem(String extensao) {
		extensao = StringUtils.lowerCase(extensao)
		return extensao != null && (extensao.equals("png") || extensao.equals("jpg") || extensao.equals("jpeg") || extensao.equals("gif"))
	}
	
	String getExtensao( String nomeDoArquivo) {
		String extensao = null
		if (ehUmNomeDeArquivoValido(nomeDoArquivo)) {
			extensao = nomeDoArquivo.substring(nomeDoArquivo.lastIndexOf('.') + 1, nomeDoArquivo.length())
		}
		return extensao
	}
	
	List<Anexo> listarAnexos( int ticketKey) {
		File folder = new File(Config.getImagesFolder() + ticketKey)
		if (folder.exists()) {
			List<Anexo> anexos = new ArrayList<Anexo>()
			String[] files = folder.list()
			Arrays.sort(files)
			for ( String file : files) {
				anexos.add(new Anexo(file))
			}
			return anexos
		} else {
			return null
		}
	}
	
	
	@RequestMapping(value = '/anexos', method = GET)
	String download( HttpServletResponse response,  String file)  {
		def ticketKey = Integer.parseInt( file.replaceAll('(.*)?\\/.*', '$1'))
		def path = file.replaceAll('.*?\\/(.*)', '$1')
		return download(response, path, ticketKey)
	}
	
	@RequestMapping(value = '/{ticketKey}/anexos', method = GET)
	String download( HttpServletResponse response,  String file,  @PathVariable int ticketKey)  {
		
		File arquivo = null
		
		try {
			arquivo = new File(Config.getImagesFolder() + ticketKey + "/" + file)
			if (!arquivo.exists()) throw new FileNotFoundException()
		} catch(e) {
			arquivo = new File(this.getClass().getResource("/noimage.gif").getFile())
		}
		
		FileInputStream fis = new FileInputStream(arquivo)
		int numberBytes = fis.available()
		byte[] bytes = new byte[numberBytes]
		fis.read(bytes)
		fis.close()
		
		String extensao = getExtensao(file)
		
		String mime = null
		if (extensao == null) {
			mime = "text/plain"
		} else if (extensao.equalsIgnoreCase("png")) {
			mime = "images/png"
		} else if (extensao.equalsIgnoreCase("jpg") || extensao.equalsIgnoreCase("jpeg")) {
			mime = "images/jpeg"
		} else if (extensao.equalsIgnoreCase("gif")) {
			mime = "images/gif"
		} else if (extensao.equalsIgnoreCase("pdf")) {
			mime = "application/pdf"
		} else if (extensao.equalsIgnoreCase("xls") || extensao.equalsIgnoreCase("xlsx")) {
			mime = "application/vnd.ms-excel"
		} else if (extensao.equalsIgnoreCase("csv")) {
			mime = "text/csv"
		} else if (extensao.equalsIgnoreCase("txt")) {
			mime = "text/plain"
		} else if (extensao.equalsIgnoreCase("doc") || extensao.equalsIgnoreCase("docx")) {
			mime = "application/ms-word"
		}
		
		response.addHeader("content-disposition", "attachment filename=" + file)
		response.setContentType(mime)
		response.setContentLength(bytes.length)
		FileCopyUtils.copy(bytes, response.getOutputStream())
		return null
	}
	
	@RequestMapping(value = "/{ticketKey}/anexos", method=DELETE)
	String excluirAnexo(@PathVariable int ticketKey, String file)  {
		File arquivo = new File(Config.getImagesFolder() + ticketKey + "/" + file)
		if (arquivo.exists()) {
			arquivo.delete()
		}
		
		Ticket ticket = ticketDao.obter(ticketKey)
		ticket.addLogDeExclusao 'anexo', file
		ticketDao.salvar ticket
		
		return "redirect:/tickets/${ticketKey}"
	}
	
	
	@RequestMapping("/{ticketKey}/salvarValorDeNegocio")
	@ResponseBody String salvarValorDeNegocio( HttpServletResponse response, @PathVariable int ticketKey,  int valorDeNegocio) throws SegurancaException {
		try {
			Seguranca.validarPermissao(Papel.PRODUCT_OWNER)
			Ticket ticket = ticketDao.obter(ticketKey)
			ticket.setValorDeNegocio(valorDeNegocio)
			ticketDao.salvar(ticket)
			return "true"  
		} catch (e) {
			return "false"
		}
	}
	
	@RequestMapping("/{ticketKey}/salvarBranch")
	@ResponseBody String salvarBranch( HttpServletResponse response, @PathVariable int ticketKey,  String branch) throws SegurancaException {
		try {
			Seguranca.validarPermissao(Papel.EQUIPE)
			Ticket ticket = ticketDao.obter(ticketKey)
			ticket.setBranch(branch)
			ticketDao.salvar(ticket)
			return "true"
		} catch (e) {
			return "false"
		}
	}
	
	@RequestMapping("/{ticketKey}/salvarEsforco")
	@ResponseBody String salvarEsforco( HttpServletResponse response, @PathVariable int ticketKey,  double esforco) throws SegurancaException {
		try {
			Seguranca.validarPermissao(Papel.EQUIPE)
			Ticket ticket = ticketDao.obter(ticketKey)
			ticket.setEsforco(esforco)
			ticketDao.salvar(ticket)
			return "true"
		} catch (e) {
			return "false"  
		}
	}
	
	@RequestMapping("/{ticketKey}/salvarPar")
	@ResponseBody String salvarPar( HttpServletResponse response, @PathVariable int ticketKey,  boolean par) throws SegurancaException {
		try {
			Seguranca.validarPermissao(Papel.EQUIPE)
			Ticket ticket = ticketDao.obter(ticketKey)
			ticket.setPar(par)
			ticketDao.salvar(ticket)
			return "true"
		} catch (e) {
			return "false"  
		}
	}
	
	@RequestMapping("/{ticketKey}/salvarCategoria")
	@ResponseBody String salvarCategoria( HttpServletResponse response, @PathVariable int ticketKey,  int categoriaKey) throws SegurancaException {
		try {
			Seguranca.validarPermissao(Papel.PRODUCT_OWNER)
			Ticket ticket = ticketDao.obter(ticketKey)
			ticket.setCategoria(categoriaDao.obter(categoriaKey))
			ticketDao.salvar(ticket)
			return "true"
		} catch (e) {
			return "false"  
		}
	}
	
	@RequestMapping("/{ticketKey}/transformarEmEstoria")
	String transformarEmEstoria( Model model,  @PathVariable int ticketKey) {
		
		Seguranca.validarPermissao(Papel.PRODUCT_OWNER, Papel.EQUIPE)
		
		Ticket ticket = ticketDao.obter(ticketKey)
		ticket.setTipoDeTicket((TipoDeTicket) sessionFactory.getCurrentSession().createCriteria(TipoDeTicket.class).add(Restrictions.eq("tipoDeTicketKey", TipoDeTicket.ESTORIA)).uniqueResult())
		ticketDao.salvar(ticket)
		
		return "redirect:/tickets/${ticketKey}"
		
	}
	
	@RequestMapping("/{ticketKey}/transformarEmDefeito")
	String transformarEmDefeito( Model model, @PathVariable int ticketKey)  {
		
		Seguranca.validarPermissao(Papel.PRODUCT_OWNER, Papel.EQUIPE)
		
		Ticket ticket = ticketDao.obter(ticketKey)
		
		if (ticket.getFilhos() != null && ticket.getFilhos().size() > 0) {
			model.addAttribute("erro", "Essa estória possui tarefas e por isso não pode ser transformada em um defeito.")
		} else {
			ticket.setTipoDeTicket((TipoDeTicket) sessionFactory.getCurrentSession().createCriteria(TipoDeTicket.class).add(Restrictions.eq("tipoDeTicketKey", TipoDeTicket.DEFEITO)).uniqueResult())
			ticketDao.salvar(ticket)
		}
		return "redirect:/tickets/${ticketKey}"
	}
	
	@RequestMapping("/{ticketKey}/descricao")
	String verDescricao( Model model, @PathVariable int ticketKey) throws Exception {
		Ticket ticket = ticketDao.obter(ticketKey)
		model.addAttribute("ticket", ticket)
		model.addAttribute("anexos", listarAnexos(ticketKey))
		return "/ticket/ticket.preview.jsp"
	}
	
	@RequestMapping(value = "/{ticketKey}", method = GET)
	String editar( Model model, @PathVariable  Integer ticketKey) throws SegurancaException {
		return editar(model, ticketKey, null, null)
	}
	
	@RequestMapping("/novo")
	String editar( Model model,  Integer ticketKey,  Integer tipoDeTicketKey,  Integer backlogKey) throws SegurancaException {
		
		Seguranca.validarPermissao(Papel.PRODUCT_OWNER, Papel.EQUIPE)
		
		if (ticketKey != null) {
			Ticket ticket = ticketDao.obterComDependecias(ticketKey)
			
			if (ticket == null) {
				model.addAttribute("mensagem", "O Ticket #" + ticketKey + " não existe.")
				return "/branca.jsp"
			}
			model.addAttribute("sprints", sprintDao.listarSprintsEmAberto())
			model.addAttribute("ticket", ticket)
			model.addAttribute("anexos", listarAnexos(ticketKey))
			
		} else {
			Ticket novoTicket = new Ticket()
			novoTicket.setReporter(Seguranca.getUsuario())
			novoTicket.setTipoDeTicket((TipoDeTicket) sessionFactory.getCurrentSession().get(TipoDeTicket.class, tipoDeTicketKey))
			novoTicket.setBacklog((Backlog) sessionFactory.getCurrentSession().get(Backlog.class, backlogKey))
			model.addAttribute("ticket", novoTicket)
			model.addAttribute("tipoDeTicketKey", tipoDeTicketKey)
		}
		
		def equipe = usuarioDao.listarEquipe()

		model.addAttribute "categorias", categoriaDao.listar()
		model.addAttribute "configuracoes", configuracaoDao.getMapa()
		model.addAttribute "clientes", clienteDao.listar()
		model.addAttribute "testadores", equipe 
		model.addAttribute "desenvolvedores", equipe
		model.addAttribute "kanbanStatus", kanbanStatusDao.listar()

		VIEW_EDITAR
	}
	
	@RequestMapping("/{paiKey}/incluirTarefa")
	String incluirTarefa( Model model, @PathVariable int paiKey) throws Exception {
		
		Seguranca.validarPermissao Papel.PRODUCT_OWNER, Papel.EQUIPE
		
		Ticket pai = ticketDao.obter(paiKey)
		
		Ticket tarefa = new Ticket()
		copiarDadosDoPai(pai, tarefa)
		tarefa.setPrioridade(9999)
		
		model.addAttribute("ticket", tarefa)
		model.addAttribute("tipoDeTicketKey", TipoDeTicket.TAREFA)
		model.addAttribute("kanbanStatus", kanbanStatusDao.listar())
		model.addAttribute("testadores", usuarioDao.listarEquipe())
		model.addAttribute("desenvolvedores", usuarioDao.listarEquipe())
		
		return VIEW_EDITAR
	}
	
	void copiarDadosDoPai( Ticket pai,  Ticket filho) {
		filho.setPai(pai)
		if (filho.getReporter() == null || filho.getReporter().getUsername() == null) {
			filho.setReporter(Seguranca.getUsuario())
		}
		filho.setTipoDeTicket(tipoDeTicketDao.obter(TipoDeTicket.TAREFA))
		filho.setBacklog(pai.getBacklog())
		filho.setSprint(pai.getSprint())
		filho.setCliente(pai.getCliente())
		filho.setSolicitador(pai.getSolicitador())
	}
	
	@RequestMapping(value="/{pai}/ordenar", method=POST)
	void alterarOrdem(@PathVariable int pai,  int[] ticketKey) {
		Ticket ticketPai = ticketDao.obterComDependecias(pai)
		int prioridade = 0
		for ( int filhoKey : ticketKey) {
			Ticket ticketFilho = ticketPai.getFilhoPorKey(filhoKey)
			if (ticketFilho != null) {
				ticketFilho.setPrioridade(++prioridade)
				ticketDao.salvar(ticketFilho)
			}
		}
	}
	
	@RequestMapping("/{ticketKey}/log/{ticketHistoryKey}")
	String logDescricao( Model model, @PathVariable int ticketHistoryKey) {
		model.addAttribute "log", sessionFactory.currentSession.get(TicketLog.class, ticketHistoryKey)
		return "/ticket/ticket.logDescricao.jsp"
	}
}
