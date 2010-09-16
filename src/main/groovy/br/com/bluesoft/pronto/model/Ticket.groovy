package br.com.bluesoft.pronto.model

import java.sql.Timestamp;
import java.util.ArrayList
import java.util.Date
import java.util.LinkedList;
import java.util.List
import java.util.Set

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.OrderBy
import javax.persistence.SequenceGenerator
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade
import org.springframework.beans.factory.annotation.Autowired;

import br.com.bluesoft.pronto.annotations.Auditable;
import br.com.bluesoft.pronto.annotations.Label
import br.com.bluesoft.pronto.core.Backlog
import br.com.bluesoft.pronto.core.KanbanStatus
import br.com.bluesoft.pronto.core.TipoDeTicket
import br.com.bluesoft.pronto.service.Seguranca
import br.com.bluesoft.pronto.service.WikiFormatter
import br.com.bluesoft.pronto.util.DateUtil;

import com.google.common.collect.HashMultiset
import com.google.common.collect.Multiset

@Entity
@SequenceGenerator(name = "SEQ_TICKET", sequenceName = "SEQ_TICKET")
class Ticket {
	
	public static String BRANCH_MASTER = "master"
	
	public Ticket() {
		// Inicializando campos para binding do Spring.
		tipoDeTicket = new TipoDeTicket(TipoDeTicket.ESTORIA)
		backlog = new Backlog(Backlog.PRODUCT_BACKLOG)
		reporter = new Usuario()
		sprint = new Sprint()
		kanbanStatus = new KanbanStatus(KanbanStatus.TO_DO)
		comentarios = new ArrayList<TicketComentario>()
		logs = new ArrayList<TicketLog>()
		script = new Script()
	}
	
	@Id
	@GeneratedValue(generator = "SEQ_TICKET")
	int ticketKey
	
	@Auditable
	@Label("título")
	String titulo
	
	@Auditable
	@Label("backlog")
	@ManyToOne
	@JoinColumn(name = "BACKLOG_KEY")
	Backlog backlog
	
	@ManyToOne
	@JoinColumn(name = "KANBAN_STATUS_KEY")
	KanbanStatus kanbanStatus
	
	@Auditable
	@ManyToOne
	@JoinColumn(name = "TIPO_DE_TICKET_KEY")
	@Label("tipo de ticket")
	TipoDeTicket tipoDeTicket
	
	@Auditable
	@Label("descrição")
	String descricao
	
	@Auditable
	@ManyToOne
	@JoinColumn(name = "REPORTER_KEY")
	Usuario reporter
	
	@Auditable
	@Label("cliente")
	@ManyToOne
	@JoinColumn(name = "CLIENTE_KEY")
	Cliente cliente
	
	@Auditable
	String solicitador
	
	@Auditable
	String branch
	
	@Auditable
	@Label("desenvolvedores")
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "TICKET_DESENVOLVEDOR", joinColumns =  @JoinColumn(name = "TICKET_KEY") , inverseJoinColumns =  @JoinColumn(name = "USUARIO_KEY") )
	Set<Usuario> desenvolvedores
	
	@Auditable
	@Label("testadores")
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "TICKET_TESTADOR", joinColumns =  @JoinColumn(name = "TICKET_KEY") , inverseJoinColumns =  @JoinColumn(name = "USUARIO_KEY") )
	Set<Usuario> testadores
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="ticket")
	List<MovimentoKanban> movimentosDeKanban
	
	@Auditable
	@Label("valor de negócio")
	int valorDeNegocio
	
	@Auditable
	@Label("esforço")
	double esforco
	
	@Auditable
	@Label("em par?")
	boolean par
	
	@Auditable
	@Label("planejado?")
	boolean planejado
	
	@ManyToOne
	@JoinColumn(name = "pai")
	@Cascade(org.hibernate.annotations.CascadeType.LOCK)
	Ticket pai
	
	@Auditable
	@Label("categoria")
	@ManyToOne
	@JoinColumn(name = "categoria_key")
	Categoria categoria
	
	@Auditable
	@Label("causa do defeito")
	@ManyToOne
	@JoinColumn(name = "causa_de_defeito_key")
	CausaDeDefeito causaDeDefeito
	
	@OneToMany(mappedBy = "pai", cascade = CascadeType.ALL)
	@OrderBy("prioridade asc")
	@Cascade(org.hibernate.annotations.CascadeType.LOCK)
	List<Ticket> filhos
	
	@Auditable
	@Label("data de pronto")
	Date dataDePronto
	
	@Label("data da última alteração")
	Timestamp dataDaUltimaAlteracao
	
	@Label("data de criaçao")
	Date dataDeCriacao
	
	@Auditable
	@Label("prioridade de desenvolvimento")
	Integer prioridade
	
	@Auditable
	@Label("prioridade do cliente")
	Integer prioridadeDoCliente
	
	@Auditable
	@ManyToOne
	@JoinColumn(name = "sprint")
	Sprint sprint
	
	@OneToOne
	@JoinColumn(name = "script_key")
	Script script
	
	@OneToOne
	@JoinColumn(name = "ticket_origem_key")
	Ticket ticketOrigem
	
	@OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
	final List<TicketLog> logs
	
	@OrderBy("data asc")
	@OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
	List<TicketComentario> comentarios
	
	String getTituloResumido() {
		
		if (titulo != null) {
			final int tamanhoMaximo = 45
			if (titulo.length() > tamanhoMaximo) {
				return titulo.substring(0, tamanhoMaximo) + "..."
			}
		}
		
		return titulo
	}
	
	String getHtml() {
		return WikiFormatter.toHtml(descricao)
	}
	
	double getSomaDoEsforcoDosFilhos() {
		if (temFilhos()) {
			double soma = 0d
			for (final Ticket filho : filhos) {
				soma += filho.getEsforco()
			}
			return soma
		}
		return 0d
	}
	
	double getEsforco() {
		if (temFilhos()) {
			return getSomaDoEsforcoDosFilhos()
		} else {
			return new BigDecimal(esforco).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue()
		}
	}
	
	void setEsforco(final double esforco) {
		this.esforco = new BigDecimal(esforco).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue()
	}
	
	Integer getPrioridade() {
		return prioridade != null ? prioridade : 0
	}
	
	@Transient
	Integer getTempoDeVidaEmDias() {
		if (dataDePronto) {
			return dataDePronto - dataDeCriacao
		} else {
			return new Date() - dataDeCriacao
		}
	}
	
	@Override
	String toString() {
		return "#" + getTicketKey() + " - " + titulo
	}
	
	void addComentario(final String texto, final Usuario usuario) {
		final TicketComentario comentario = new TicketComentario()
		comentario.setTicket(this)
		comentario.setData(new Date())
		comentario.setUsuario(usuario)
		comentario.setTexto(texto)
		comentarios.add(comentario)
	}
	
	void addLogDeAlteracao(final String campo, final String valorAntigo, final String valorNovo) {
		final TicketLog log = new TicketLog()
		log.setTicket(this)
		log.setData(new Date())
		log.setUsuario(Seguranca.getUsuario().getUsername())
		log.setOperacao(TicketLog.ALTERACAO)
		log.setCampo(campo)
		log.setValorAntigo(valorAntigo)
		log.setValorNovo(valorNovo)
		logs.add(log)
	}
	
	void addLogDeInclusao(final String campo, final String valor) {
		final TicketLog log = new TicketLog()
		log.setTicket(this)
		log.setData(new Date())
		log.setUsuario(Seguranca.getUsuario().getUsername())
		log.setOperacao(TicketLog.INCLUSAO)
		log.setCampo(campo)
		log.setValorAntigo(null)
		log.setValorNovo(valor)
		logs.add(log)
	}
	
	void addLogDeExclusao(final String campo, final String valor) {
		final TicketLog log = new TicketLog()
		log.setTicket(this)
		log.setData(new Date())
		log.setUsuario(Seguranca.getUsuario().getUsername())
		log.setOperacao(TicketLog.EXCLUSAO)
		log.setCampo(campo)
		log.setValorAntigo(valor)
		log.setValorNovo(null)
		logs.add(log)
	}
	
	
	boolean isSprintBacklog() {
		return getBacklog() != null && getBacklog().getBacklogKey() == Backlog.SPRINT_BACKLOG
	}
	
	void addDesenvolvedor(final Usuario usuario) {
		if (!desenvolvedores) desenvolvedores = [] as HashSet;
		desenvolvedores.each {
			if (it.username.equals(usuario.username))
				return
		}
		desenvolvedores.add(usuario)
	}
	
	void addTestador(final Usuario usuario) {
		if (!testadores) testadores = [] as HashSet;
		testadores.each {
			if (it.username.equals(usuario.username))
				return
		}
		testadores.add(usuario)
	}
	
	boolean isDefeito() {
		return getTipoDeTicket() != null && getTipoDeTicket().getTipoDeTicketKey() == TipoDeTicket.DEFEITO
	}
	
	boolean isEstoria() {
		return getTipoDeTicket() != null && getTipoDeTicket().getTipoDeTicketKey() == TipoDeTicket.ESTORIA
	}
	
	boolean isEstoriaSemTarefa() {
		return isEstoria() && !temFilhos()
	}
	
	boolean isTarefa() {
		return getTipoDeTicket() != null && getTipoDeTicket().getTipoDeTicketKey() == TipoDeTicket.TAREFA
	}
	
	boolean isDone() {
		return kanbanStatus != null && kanbanStatus.getKanbanStatusKey() == KanbanStatus.DONE
	}
	
	boolean isToDo() {
		return kanbanStatus != null && kanbanStatus.getKanbanStatusKey() == KanbanStatus.TO_DO
	}
	
	boolean isIdeia() {
		return getTipoDeTicket() != null && getTipoDeTicket().getTipoDeTicketKey() == TipoDeTicket.IDEIA
	}
	
	boolean temFilhos() {
		if (isTarefa()) {
			return false
		} else {
			return filhos != null && filhos.size() > 0
		}
	}
	
	boolean temPai() {
		if (!isTarefa()) {
			return false
		} else {
			return pai != null
		}
	}
	
	boolean isLixo() {
		return backlog != null && backlog.getBacklogKey() == Backlog.LIXEIRA
	}
	
	boolean isImpedido() {
		return backlog != null && backlog.getBacklogKey() == Backlog.IMPEDIMENTOS
	}
	
	
	
	boolean isTodosOsFilhosProntos() {
		if (temFilhos()) {
			for (final Ticket filho : filhos) {
				if (!filho.isDone()) {
					return false
				}
			}
		}
		return true
	}
	
	String getDescricaoPar() {
		return isPar() ? "Par" : "Solo"
	}
	
	boolean isEmAndamento() {
		if (temFilhos()) {
			final Multiset<Integer> contadorDeStatus = HashMultiset.create()
			final int quantidadeDeFilhos = filhos.size()
			for (final Ticket filho : filhos) {
				contadorDeStatus.add(filho.getKanbanStatus().getKanbanStatusKey())
			}
			
			if (contadorDeStatus.count(KanbanStatus.DONE) != quantidadeDeFilhos && contadorDeStatus.count(KanbanStatus.TO_DO) != quantidadeDeFilhos) {
				return true
			} else {
				return false
			}
		} else {
			return !isDone() && !isToDo()
		}
	}
	
	Ticket getFilhoPorKey(final int filhoKey) {
		for (final Ticket f : filhos) {
			if (f.getTicketKey() == filhoKey) {
				return f
			}
		}
		return null
	}
	
	int getMaiorPrioridade() {
		int maiorPrioridade = 0
		for (final Ticket f : filhos) {
			maiorPrioridade = Math.max(maiorPrioridade, f.getPrioridade() == null ? 0 : f.getPrioridade())
		}
		return maiorPrioridade
	}
	
	int getMenorPrioridade() {
		int menorPrioridade = 0
		for (final Ticket f : filhos) {
			menorPrioridade = Math.min(menorPrioridade, f.getPrioridade() == null ? 0 : f.getPrioridade())
		}
		return menorPrioridade
	}
	
	List<Ticket> getFilhos() {
		filhos
	}
	
	List<Ticket> getFilhosOrdenadosKanbanStatus() {
		filhos.sort { it.kanbanStatus.kanbanStatusKey  }
	}
	
	List<Ticket> getFilhosProntos() {
		filhos.findAll { it.done }
	}
	
	List<TicketLog> getLogs() {
		logs.sort { it.data }
	}
	
	public void setDesenvolvedores(List<Usuario> desenvolvedores) {
		this.desenvolvedores = new LinkedList(desenvolvedores);
	}
	
	public void setDesenvolvedores(Collection<Usuario> desenvolvedores) {
		this.desenvolvedores = new LinkedList(desenvolvedores);
	}
	
	public void setTestadores(Collection<Usuario> testadores) {
		this.testadores = new LinkedList(testadores);
	}
	
	public void setTestadores(List<Usuario> testadores) {
		this.testadores = new LinkedList(testadores);
	}

	public void setTicketOrigem(Ticket ticketOrigem) {
		this.ticketOrigem = ticketOrigem;
	}	
	
	def getEnvolvidos() {
		def envolvidos = [] as Set
		envolvidos << this.reporter
		if (this.getDesenvolvedores()) {
			envolvidos.addAll this.getDesenvolvedores()
		}
		if (this.getTestadores()) {
			envolvidos.addAll this.getTestadores()
		}
		return envolvidos
	}
}
