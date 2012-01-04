package br.com.bluesoft.pronto.dao;

import java.sql.Timestamp
import java.util.ArrayList
import java.util.Collections
import java.util.Comparator
import java.util.Date
import java.util.List

import org.apache.commons.beanutils.BeanComparator
import org.apache.commons.collections.comparators.ComparatorChain
import org.apache.commons.collections.comparators.ReverseComparator
import org.hibernate.Query
import org.springframework.stereotype.Repository

import br.com.bluesoft.pronto.core.Backlog
import br.com.bluesoft.pronto.core.TipoDeTicket
import br.com.bluesoft.pronto.model.Classificacao
import br.com.bluesoft.pronto.model.Sprint
import br.com.bluesoft.pronto.model.Ticket
import br.com.bluesoft.pronto.model.TicketComentario
import br.com.bluesoft.pronto.model.TicketOrdem
import br.com.bluesoft.pronto.model.Usuario
import br.com.bluesoft.pronto.service.GeradorDeLogDeTicket
import br.com.bluesoft.pronto.service.Seguranca
import br.com.bluesoft.pronto.to.DashboardItem
import br.com.bluesoft.pronto.to.Entry
import br.com.bluesoft.pronto.to.ReleaseNote
import br.com.bluesoft.pronto.to.TicketFilter
import br.com.bluesoft.pronto.util.DateUtil

@Repository
public class TicketDao extends DaoHibernate {
	
	@Override
	public Ticket obter(final Integer ticketKey) {
		
		final StringBuilder hql = new StringBuilder();
		hql.append("select distinct t from Ticket t ");
		hql.append("left join fetch t.sprint        ");
		hql.append("left join fetch t.pai           ");
		hql.append("left join fetch t.tipoDeTicket  ");
		hql.append("left join fetch t.backlog       ");
		hql.append("left join fetch t.kanbanStatus  ");
		hql.append("left join fetch t.reporter      ");
		hql.append("where t.ticketKey = :ticketKey  ");
		
		return (Ticket) getSession().createQuery(hql.toString()).setInteger("ticketKey", ticketKey).uniqueResult();
	}
	
	@Override
	public Ticket obterPorStatus(final Integer ticketKey, final Integer kanbanStatusKey) {

		final StringBuilder hql = new StringBuilder();
		hql.append("select distinct t from Ticket t ");
		hql.append("left join fetch t.sprint ");
		hql.append("left join fetch t.pai ");
		hql.append("left join fetch t.tipoDeTicket ");
		hql.append("left join fetch t.backlog ");
		hql.append("left join fetch t.kanbanStatus ");
		hql.append("left join fetch t.reporter ");
		hql.append("where t.ticketKey = :ticketKey ");
		hql.append("and t.kanbanStatus.kanbanStatusKey = :kanbanStatusKey");

		return (Ticket) getSession().createQuery(hql.toString()).setInteger("ticketKey", ticketKey).setInteger("kanbanStatusKey", kanbanStatusKey).uniqueResult();
	}
	
	
	public TicketComentario obterComentario(int ticketComentarioKey){
		final StringBuilder hql = new StringBuilder();
		hql.append(" select distinct tc from TicketComentario tc ");
		hql.append(" where tc.ticketComentarioKey = :ticketComentarioKey ");
		return (TicketComentario) getSession().createQuery(hql.toString()).setInteger("ticketComentarioKey", ticketComentarioKey).uniqueResult();
	}
	
	void excluirComentario(int ticketComentarioKey) {
		def comentario = session.get(TicketComentario.class, ticketComentarioKey)
		session.delete(comentario)
		session.flush()
	}
	
	@Override
	public Ticket obterTicketPronto(final Integer ticketKey) {

		final StringBuilder hql = new StringBuilder();
		hql.append("select distinct t from Ticket t ");
		hql.append("left join fetch t.sprint ");
		hql.append("left join fetch t.pai ");
		hql.append("left join fetch t.tipoDeTicket ");
		hql.append("left join fetch t.backlog ");
		hql.append("left join fetch t.kanbanStatus ");
		hql.append("left join fetch t.reporter ");
		hql.append("where t.ticketKey = :ticketKey ");
		hql.append("and t.kanbanStatus.fim = true");

		return (Ticket) getSession().createQuery(hql.toString()).setInteger("ticketKey", ticketKey).setInteger("kanbanStatusKey", kanbanStatusKey).uniqueResult();
	}
	
	@Override
	public Ticket obterComUsuariosEnvolvidos(final Integer ticketKey) {
		
		String hql = """ 
			select distinct t from Ticket t 
			left join fetch t.sprint 
			left join fetch t.pai 
			left join fetch t.tipoDeTicket 
			left join fetch t.backlog 
			left join fetch t.kanbanStatus 
			left join fetch t.reporter 
			left join t.envolvidos 
			where t.ticketKey = :ticketKey
		"""
		
		return (Ticket) getSession()
		.createQuery(hql)
		.setInteger("ticketKey", ticketKey)
		.uniqueResult();
	}
	
	public Ticket obterComDependecias(final Integer ticketKey) {
		
		String hql = """ 
			select distinct t from Ticket t 
			left join fetch t.sprint 
			left join fetch t.pai 
			left join fetch t.tipoDeTicket 
			left join fetch t.backlog 
			left join fetch t.kanbanStatus 
			left join fetch t.reporter 
			left join fetch t.filhos 
			left join t.envolvidos 
			left join t.comentarios 
			left join t.logs 
			where t.ticketKey = :ticketKey
		"""
		
		return (Ticket) getSession()
		.createQuery(hql)
		.setInteger("ticketKey", ticketKey)
		.uniqueResult();
	}
	
	public TicketDao() {
		super(Ticket.class);
	}
	
	@Override
	public void salvar(final Ticket... tickets) {
		prepararParaSalvar(tickets)
		super.salvar(tickets)
		session.flush()
		session.clear()
		defineValores(tickets)
		session.flush()
	}
	
	
	private void prepararParaSalvar(final Ticket... tickets) {
		for (final Ticket ticket : tickets) {
			if (ticket.getScript() != null && ticket.getScript().getScriptKey() == 0) {
				ticket.setScript(null);
			}
			
			if(ticket.ticketKey == 0) {
				ticket.prioridade = 999999
			}
			
			if (ticket.ticketKey > 0) {
				Ticket antigo = obter(ticket.ticketKey)
				def logs = GeradorDeLogDeTicket.gerarLogs(antigo, ticket)
				ticket.logs.addAll(logs)
			}
		}
		session.clear()
	}
	
	private void defineValores(final Ticket... tickets) {
		
		for (Ticket ticket : tickets) {
			
			ticket = obter(ticket.ticketKey)
			
			ticket.dataDaUltimaAlteracao = DateUtil.getTimestampSemMilissegundos(new Timestamp(new Date().getTime()))
			
			if (ticket.getReporter() == null) {
				ticket.setReporter(Seguranca.getUsuario());
			}
			
			if (ticket.getScript() != null && ticket.getScript().getScriptKey() == 0) {
				ticket.setScript(null);
			}
			
			// Se um ticket tiver filhos, atualizar os dados dos filhos que devem ser sempre iguais aos do pai.
			if (ticket.temFilhos()) {
				ticket.setEsforco(ticket.getSomaDoEsforcoDosFilhos());
				
				for (Ticket filho : ticket.getFilhos()) {
					if (!filho.isImpedido() && !filho.isLixo()) {
						filho.setBacklog(ticket.getBacklog());
					}
					filho.setCliente(ticket.getCliente());
					filho.setSolicitador(ticket.getSolicitador());
					filho.setSprint(ticket.getSprint());
					filho.setTipoDeTicket((TipoDeTicket) getSession().get(TipoDeTicket.class, TipoDeTicket.TAREFA));
				}
				
				if (ticket.isTodosOsFilhosProntos()) {
					if (ticket.getDataDePronto() == null) {
						ticket.setDataDePronto(new Date());
					}
				} else {
					ticket.setDataDePronto(null);
				}
			}
			
			// Se o ticket pai estiver impedido ou na lixeira, o ticket filho deve permancer da mesma forma.
			if (ticket.temPai()) {
				
				final Ticket pai = ticket.pai
				
				if (pai.isLixo() || pai.isImpedido()) {
					ticket.setBacklog(pai.getBacklog())
				} else {
					if (!ticket.isLixo() && !ticket.isImpedido()) {
						ticket.setBacklog(pai.getBacklog())
					}
				}
				
				ticket.setSprint(pai.getSprint())
				ticket.setTipoDeTicket((TipoDeTicket) getSession().get(TipoDeTicket.class, TipoDeTicket.TAREFA))
				
				if (ticket.isDone() && pai.isTodosOsFilhosProntos()) {
					if (pai.getDataDePronto() == null) {
						pai.setDataDePronto(new Date())
						pai.setKanbanStatus(ticket.kanbanStatus)
						super.getSession().update(pai)
					}
				} else {
					if (!pai.isEmAndamento()) {
						pai.setKanbanStatus(pai.projeto.getEtapaToDo())
					}
					pai.setDataDePronto(null)
				}
			}
			
			// Tarefa nao tem valor de Negocio
			if (ticket.isTarefa()) {
				ticket.setValorDeNegocio(0)
			}
			
			// Grava sysdate na criação
			if (ticket.getDataDeCriacao() == null) {
				ticket.setDataDeCriacao(new Date())
			}

			
			if (ticket.getKanbanStatus() == null && ticket.projeto != null) {
				ticket.kanbanStatus = ticket.projeto.getEtapaToDo()
			}
						
			// Se o status for pronto tem que ter data de pronto.
			if (ticket.getKanbanStatus() != null && ticket.getKanbanStatus().isFim() && ticket.getDataDePronto() == null) {
				ticket.setDataDePronto(new Date())
			}
			
			getSession().saveOrUpdate(ticket)
		}
	}
	
	public List<Ticket> buscar(TicketFilter filtro) {
		
		final StringBuilder hql = new StringBuilder();
		hql.append(" select distinct t from Ticket t   ");
		hql.append(" left join fetch t.sprint          ");
		hql.append(" left join fetch t.reporter r      ");
		hql.append(" left join fetch t.responsavel resp ");
		hql.append(" left join fetch t.categoria cat   ");
		hql.append(" left join fetch t.projeto projeto ");
		hql.append(" left join fetch t.modulo mod ");
		hql.append(" left join fetch t.milestone ml ");
		hql.append(" left join fetch t.tipoDeTicket as tipoDeTicket ");
		hql.append(" left join fetch t.backlog as b    ");
		hql.append(" left join fetch t.kanbanStatus as kanbanStatus ");
		hql.append(" left join fetch t.filhos          ");
		hql.append(" left join fetch t.cliente as cliente  ");
		hql.append(" left join fetch t.envolvidos as envolvidos");
		hql.append(" left join t.comentarios as comentarios");
		hql.append(" where 1=1 ");
		
		if (filtro.query!=null) {
			hql.append(" and upper(t.titulo) like :query ");
		}
		
		if (filtro.kanbanStatusKey != null) {
			if (filtro.kanbanStatusKey == -1) {
				hql.append(" and t.dataDePronto is null ");
			} else if (filtro.kanbanStatusKey > 0) {
				hql.append(" and t.kanbanStatus.kanbanStatusKey = :kanbanStatusKey ");
			}
		}
		
		if (filtro.clienteKey != null && filtro.clienteKey > 0) {
			hql.append(" and t.cliente.clienteKey = :clienteKey ");
		}
		
		if (filtro.sprintKey != null && filtro.sprintKey > 0) {
			hql.append(" and t.sprint.sprintKey = :sprintKey ");
		}
		
		if (filtro.sprintNome != null && filtro.sprintNome.length() > 0) {
			hql.append(" and upper(t.sprint.nome) like :sprintNome ");
		}
		
		if (filtro.backlogKey && filtro.backlogKey > 0) {
			hql.append(" and b.backlogKey = :backlogKey ");
		} else  if (filtro.ignorarLixeira != null && filtro.ignorarLixeira) {
			hql.append(" and b.backlogKey <> :backlogKey ");
		}
		
		if (filtro.tipoDeTicketKey && filtro.tipoDeTicketKey > 0) {
			hql.append(" and tipoDeTicket.tipoDeTicketKey = :tipoDeTicketKey ");
		}
		
		if (filtro.categoriaKey && filtro.categoriaKey > 0) {
			hql.append(" and cat.categoriaKey = :categoriaKey ");
		}
		
		if (filtro.projetoKey && filtro.projetoKey > 0) {
			hql.append(" and projeto.projetoKey = :projetoKey ");
		}
		
		if (filtro.moduloKey && filtro.moduloKey > 0) {
			hql.append(" and mod.moduloKey = :moduloKey ");
		}
		
		if (filtro.milestoneKey && filtro.milestoneKey > 0) {
			hql.append(" and ml.milestoneKey = :milestoneKey ");
		}
		
		if (filtro.reporter && filtro.reporter.length() > 0) {
			hql.append(" and r.username = :reporter ");
		}
		
		if (filtro.responsavel && filtro.responsavel.length() > 0) {
			hql.append(" and resp.username = :responsavel ");
		}
		if (filtro.envolvido && filtro.envolvido.length() > 0){
			hql.append(" and (");
			hql.append(" envolvidos.username = :envolvido ");
			hql.append(" or comentarios.usuario.username = :envolvido ");
			hql.append(" or resp.username = :envolvido ");
			hql.append(" or r.username = :envolvido ");
			hql.append(" ) ");
		}
		
		if (filtro.dataInicialCriacao) {
			hql.append(" and t.dataDeCriacao >= :dataInicialCriacao ");
		}
		
		if (filtro.dataFinalCriacao) {
			hql.append(" and t.dataDeCriacao <= :dataFinalCriacao ");
		}
		
		if (filtro.dataInicialPronto) {
			hql.append(" and t.dataDePronto >= :dataInicialPronto ");
		}
		
		if (filtro.dataFinalPronto) {
			hql.append(" and t.dataDePronto <= :dataFinalPronto ");
		}
		
		hql.append(buildOrdem(filtro.ordem, filtro.classificacao));
		
		
		final Query query = getSession().createQuery(hql.toString())
		
		if (filtro.query!=null) {
			query.setString("query", '%' + filtro.query.toUpperCase() + '%')
		}
		
		if (filtro.kanbanStatusKey != null && filtro.kanbanStatusKey > 0) {
			query.setInteger("kanbanStatusKey", filtro.kanbanStatusKey)
		}
		
		if (filtro.sprintKey != null && filtro.sprintKey > 0) {
			query.setInteger("sprintKey", filtro.sprintKey)
		}
		
		if (filtro.clienteKey != null && filtro.clienteKey > 0) {
			query.setInteger("clienteKey", filtro.clienteKey)
		}
		
		if (filtro.sprintNome != null && filtro.sprintNome.length() > 0) {
			query.setString("sprintNome", '%' + filtro.sprintNome.toUpperCase() + '%')
		}
		
		if (filtro.backlogKey) {
			query.setInteger("backlogKey", filtro.backlogKey)
		} else if (filtro.ignorarLixeira != null && filtro.ignorarLixeira) {
			query.setInteger("backlogKey", Backlog.LIXEIRA)
		}
		
		if (filtro.tipoDeTicketKey) {
			query.setInteger("tipoDeTicketKey", filtro.tipoDeTicketKey)
		}
		
		if (filtro.categoriaKey) {
			query.setInteger("categoriaKey", filtro.categoriaKey)
		}
		
		if (filtro.projetoKey) {
			query.setInteger("projetoKey", filtro.projetoKey)
		}
		
		if (filtro.moduloKey) {
			query.setInteger("moduloKey", filtro.moduloKey)
		}
		
		if (filtro.milestoneKey) {
			query.setInteger("milestoneKey", filtro.milestoneKey)
		}
		
		if (filtro.reporter) {
			query.setString("reporter", filtro.reporter)
		}
		
		if (filtro.responsavel) {
			query.setString("responsavel", filtro.responsavel)
		}
		
		if (filtro.envolvido) {
			query.setString("envolvido", filtro.envolvido)
		}
		
		if (filtro.dataInicialCriacao) {
			query.setDate("dataInicialCriacao", filtro.dataInicialCriacao)
		}
		
		if (filtro.dataFinalCriacao) {
			query.setDate("dataFinalCriacao", filtro.dataFinalCriacao)
		}
		
		if (filtro.dataInicialPronto) {
			query.setDate("dataInicialPronto", filtro.dataInicialPronto)
		}
		
		if (filtro.dataFinalPronto) {
			query.setDate("dataFinalPronto", filtro.dataFinalPronto)
		}
		
		return query.list();
	}
	
	private String buildOrdem(final TicketOrdem ordem, final Classificacao classificacao) {
		String hqlOrdem = null;
		if (ordem != null) {
			switch (ordem) {
				case TicketOrdem.BACKLOG:
					hqlOrdem = "b.descricao";
					break;
				case TicketOrdem.CODIGO:
					hqlOrdem = "t.ticketKey";
					break;
				case TicketOrdem.CLIENTE:
					hqlOrdem = "cliente.nome";
					break;
				case TicketOrdem.ESFORCO:
					hqlOrdem = "t.esforco";
					break;
				case TicketOrdem.STATUS:
					hqlOrdem = "kanbanStatus.descricao";
					break;
				case TicketOrdem.TIPO:
					hqlOrdem = "tipoDeTicket.descricao";
					break;
				case TicketOrdem.VALOR_DE_NEGOCIO:
					hqlOrdem = "t.valorDeNegocio";
					break;
				case TicketOrdem.PRIORIDADE_DO_CLIENTE:
					hqlOrdem = "cliente.nome, t.prioridadeDoCliente";
					break;
				default:
					hqlOrdem = "t.titulo";
					break;
			}
		} else {
			hqlOrdem = "t.titulo ";
		}
		final String hqlClassificacao = classificacao == null || classificacao == Classificacao.ASCENDENTE ? " asc" : " desc";
		return " order by " + hqlOrdem + hqlClassificacao;
	}
	
	public List<Ticket> listarPorSprint(final int sprintKey) {
		
		final StringBuilder builder = new StringBuilder();
		builder.append(" select distinct t from Ticket t");
		builder.append(" where t.sprint.sprintKey = :sprintKey");
		builder.append(" order by t.valorDeNegocio desc, t.esforco desc");
		
		return getSession().createQuery(builder.toString()).setInteger("sprintKey", sprintKey).list();
	}
	
	public List<Ticket> listarNaoConcluidosPorSprint(final int sprintKey) {
		final List<Ticket> ticketsDoSprint = listarPorSprint(sprintKey);
		final List<Ticket> ticketsNaoConcluidos = new ArrayList<Ticket>();
		for (final Ticket ticket : ticketsDoSprint) {
			if (!ticket.getKanbanStatus().isFim()) {
				ticketsNaoConcluidos.add(ticket);
			}
		}
		return ticketsNaoConcluidos;
	}
	
	public List<Ticket> listarPorBacklog(final int backlogKey) {
		
		final StringBuilder builder = new StringBuilder();
		builder.append(" select distinct t from Ticket t");
		builder.append(" where t.backlog.backlogKey = :backlogKey");
		builder.append(" order by t.valorDeNegocio desc, t.esforco desc");
		
		return getSession().createQuery(builder.toString()).setInteger("backlogKey", backlogKey).list();
	}
	
	public List<Usuario> listarEnvolvidosDoTicket(final int ticketKey) {
		final String hql = "select t.envolvidos from Ticket t where t.ticketKey = :ticketKey";
		return getSession().createQuery(hql).setInteger("ticketKey", ticketKey).list();
	}
	
	public List<Ticket> listarEstoriasEDefeitosDoProductBacklog() {
		return listarEstoriasEDefeitosPorBacklog(Backlog.PRODUCT_BACKLOG);
	}

	public List<Ticket> listarEstoriasEDefeitosPorBacklog(final int backlogKey) {
		return listarEstoriasEDefeitosPorBacklog(backlogKey, null, null, null);
	}
		
	public List<Ticket> listarEstoriasEDefeitosPorBacklog(final int backlogKey, final Integer categoriaKey, final Integer tipoDeTicketKey, final Integer kanbanStatusKey) {
		final StringBuilder builder = new StringBuilder();
		builder.append(" select distinct t from Ticket t");
		builder.append(" inner join fetch t.kanbanStatus ");
		builder.append(" inner join fetch t.tipoDeTicket ");
		builder.append(" left join fetch t.filhos f ");
		builder.append(" left join fetch t.pai p");
		builder.append(" where t.backlog.backlogKey = :backlogKey");
		builder.append(" and t.tipoDeTicket.tipoDeTicketKey in (:tipos)");
		
		if (categoriaKey && categoriaKey != 0) {
			if(categoriaKey < 0) {
				builder.append(" and t.categoria.categoriaKey is null ");
			} else {
				builder.append(" and t.categoria.categoriaKey = :categoriaKey ");
			}
		}
		
		if (kanbanStatusKey && kanbanStatusKey != 0) {
			if (kanbanStatusKey > 0) {
				builder.append(" and t.kanbanStatus.kanbanStatusKey = :kanbanStatusKey ");
			} else {
				builder.append(" and t.kanbanStatus.fim = true ");
			}
		}
		
		def query = getSession().createQuery(builder.toString())
		query.setInteger("backlogKey", backlogKey)
		
		if (categoriaKey && categoriaKey > 0) {
			query.setInteger 'categoriaKey', categoriaKey
		}

		if(tipoDeTicketKey && tipoDeTicketKey != 0) {
			query.setParameterList("tipos", [ tipoDeTicketKey ])
		} else {
			query.setParameterList("tipos", [ TipoDeTicket.ESTORIA, TipoDeTicket.DEFEITO ])
		}
		
		if (kanbanStatusKey && kanbanStatusKey != 0) {
			if (kanbanStatusKey > 0) {
				query.setInteger 'kanbanStatusKey', kanbanStatusKey
			} 
		}
		
		final List<Ticket> lista = query.list();
		
		
		final List<Comparator> comparators = new ArrayList<Comparator>();
		comparators.add(new ReverseComparator(new BeanComparator("valorDeNegocio")));
		comparators.add(new BeanComparator("prioridade"));
		comparators.add(new ReverseComparator(new BeanComparator("esforco")));
		comparators.add(new BeanComparator("ticketKey"));
		final ComparatorChain comparatorChain = new ComparatorChain(comparators);
		
		Collections.sort(lista, comparatorChain);
		
		return lista;
	}

	public List<Ticket> listarEstoriasEDefeitosPorSprint(final int sprintKey) {
		return listarEstoriasEDefeitosPorSprint(sprintKey, null, null, null)
	}
	
	public List<Ticket> listarEstoriasEDefeitosPorSprint(final int sprintKey, final Integer categoriaKey, final Integer tipoDeTicketKey, final Integer kanbanStatusKey) {
		final StringBuilder builder = new StringBuilder();
		
		builder.append(" select distinct t from Ticket t ");
		builder.append(" inner join fetch t.sprint s ");
		builder.append(" inner join fetch t.tipoDeTicket ");
		builder.append(" inner join fetch t.backlog ");
		builder.append(" inner join fetch t.kanbanStatus ");
		builder.append(" inner join fetch t.reporter ");
		builder.append(" left join fetch t.filhos f ");
		builder.append(" left join fetch f.sprint fs ");
		builder.append(" where s.sprintKey = :sprintKey ");
		builder.append(" and t.tipoDeTicket.tipoDeTicketKey in (:tipos) ");
		
		if (categoriaKey && categoriaKey != 0) {
			if(categoriaKey < 0) {
				builder.append(" and t.categoria.categoriaKey is null ");
			} else {
				builder.append(" and t.categoria.categoriaKey = :categoriaKey ");
			}
		}
		
		if (kanbanStatusKey && kanbanStatusKey != 0) {
			if (kanbanStatusKey && kanbanStatusKey > 0) {
				builder.append(" and t.kanbanStatus.kanbanStatusKey = :kanbanStatusKey ");
			} else {
				builder.append(" and t.kanbanStatus.fim = true ");
			}
		}
		
		def query = getSession().createQuery(builder.toString())
		query.setInteger("sprintKey", sprintKey)
		
		if (categoriaKey && categoriaKey > 0) {
			query.setInteger 'categoriaKey', categoriaKey
		}

		if(tipoDeTicketKey && tipoDeTicketKey != 0) {
			query.setParameterList("tipos", [ tipoDeTicketKey ])
		} else {
			query.setParameterList("tipos", [ TipoDeTicket.ESTORIA, TipoDeTicket.DEFEITO ])
		}
		
		if (kanbanStatusKey && kanbanStatusKey != 0) {
			if (kanbanStatusKey > 0) {
				query.setInteger 'kanbanStatusKey', kanbanStatusKey
			} 
		}
		
		final List<Ticket> lista = query.list();
		
		final List<Comparator> comparators = new ArrayList<Comparator>();
		comparators.add(new ReverseComparator(new BeanComparator("valorDeNegocio")));
		comparators.add(new BeanComparator("prioridade"));
		comparators.add(new ReverseComparator(new BeanComparator("esforco")));
		comparators.add(new BeanComparator("ticketKey"));
		final ComparatorChain comparatorChain = new ComparatorChain(comparators);
		
		Collections.sort(lista, comparatorChain);
		
		return lista;
	}
	
	public List<Ticket> listarTarefasEmBacklogsDiferentesDasEstoriasPorBacklog(final int backlogKey) {
		final StringBuilder builder = new StringBuilder();
		builder.append(" select distinct t from Ticket t");
		builder.append(" inner join fetch t.sprint ");
		builder.append(" inner join fetch t.tipoDeTicket ");
		builder.append(" inner join fetch t.backlog ");
		builder.append(" inner join fetch t.kanbanStatus ");
		builder.append(" inner join fetch t.reporter ");
		builder.append(" left join fetch t.filhos f ");
		builder.append(" left join fetch t.pai ");
		builder.append(" where t.backlog.backlogKey = :backlogKey");
		builder.append(" and t.tipoDeTicket.tipoDeTicketKey = :tipoTarefa");
		builder.append(" and t.pai.backlog.backlogKey != t.backlog.backlogKey");
		builder.append(" order by t.valorDeNegocio desc, t.esforco desc");
		def query = getSession().createQuery(builder.toString())
		query.setInteger("backlogKey", backlogKey)
		query.setInteger("tipoTarefa", TipoDeTicket.TAREFA)
		return query.list();
	}
	
	public List<Ticket> listarTicketsQueNaoEstaoNoBranchMaster() {
		final StringBuilder builder = new StringBuilder();
		builder.append(" select distinct t from Ticket t");
		builder.append(" left join fetch t.filhos f ");
		builder.append(" left join fetch t.pai p");
		builder.append(" where t.filhos is empty  and t.branch is not null and t.branch != 'master' and t.branch != ''");
		builder.append(" order by t.branch, t.titulo");
		return getSession().createQuery(builder.toString()).list();
	}
	
	public List<TicketComentario> listarUltimosComentarios(int quantos) {
		return listarUltimosComentarios(quantos, null)
	}
	
	public List<TicketComentario> listarUltimosComentarios(int quantos, String username) {
		String hql = " from TicketComentario tc"
		if (username) {
			hql+= " where tc.usuario.username = :username"
		}
		hql+= " order by tc.data desc "
		def query = getSession().createQuery(hql).setMaxResults(quantos)
		if (username) {
			query.setString "username", username
		}
		return query.list()
	}
	
	public List<Ticket> listarPorCliente(final int clienteKey) {
		final StringBuilder builder = new StringBuilder();
		builder.append(" select distinct t from Ticket t");
		builder.append(" left join fetch t.filhos f ");
		builder.append(" left join fetch t.pai p");
		builder.append(" where t.backlog.backlogKey != 4 and t.pai is null");
		builder.append(" and t.cliente.clienteKey = :clienteKey");
		builder.append(" order by t.prioridadeDoCliente");
		final Query query = getSession().createQuery(builder.toString());
		query.setInteger("clienteKey", clienteKey);
		return query.list();
	}
	
	public void alterarPrioridadeDoCliente(final int clienteKey, final Integer[] tickets) {
		
		final String sql = "update ticket set prioridade_do_cliente = :prioridade where ticket_key =:ticketKey and cliente_key = :clienteKey";
		int prioridade = 0;
		if (tickets != null) {
			for (int i = 0; i < tickets.length; i++) {
				if (tickets[i] != null) {
					getSession().createSQLQuery(sql).setInteger("ticketKey", tickets[i]).setInteger("clienteKey", clienteKey).setInteger("prioridade", ++prioridade).executeUpdate();
				}
			}
		}
	}
	
	
	void priorizar(Integer[] tickets, int valor) {
		final String sql = "update ticket set prioridade = :prioridade, valor_de_negocio = :valor where ticket_key =:ticketKey";
		int prioridade = 0;
		if (tickets != null) {
			for (int i = 0; i < tickets.length; i++) {
				if (tickets[i] != null) {
					getSession().createSQLQuery(sql)
							.setInteger("ticketKey", tickets[i])
							.setInteger("prioridade", ++prioridade)
							.setInteger('valor',valor)
							.executeUpdate();
				}
			}
		}
	}
	
	void priorizarTarefas(Integer[] tickets) {
		final String sql = "update ticket set prioridade = :prioridade where ticket_key =:ticketKey";
		int prioridade = 0;
		if (tickets != null) {
			for (int i = 0; i < tickets.length; i++) {
				if (tickets[i] != null) {
					getSession().createSQLQuery(sql)
							.setInteger("ticketKey", tickets[i])
							.setInteger("prioridade", ++prioridade)
							.executeUpdate();
				}
			}
		}
	}
	
	
	public void moverParaSprintAtual(final List<Ticket> ticketsParaMover, final Sprint sprintAtual) {
		for (final Ticket ticket : ticketsParaMover) {
			ticket.setSprint(sprintAtual);
		}
	}
	
	public Date obterDataDaUltimaAlteracaoDoTicket(int ticketKey) {
		Query query = session.createQuery('select t.dataDaUltimaAlteracao from Ticket t where t.ticketKey = :ticketKey')
		query.setInteger 'ticketKey', ticketKey
		return query.uniqueResult()
	}
	
	public Map<Integer,Integer> listarKanbanStatusDosTicketsDoSprint(int sprintKey){
		def query = session.createQuery('select t.ticketKey, t.kanbanStatus.kanbanStatusKey from Ticket t where t.sprint.sprintKey = :sprintKey');
		query.setInteger 'sprintKey', sprintKey
		def mapa = [:]
		def list = query.list()
		list.each {
			mapa[it[0] as Integer] = it[1] as Integer
		}
		return mapa
	}
	
	Integer obterTicketKeyIntegradoComZendesk(int zendeskTicketKey){
		def query = session.createSQLQuery('select izd.ticket_key from integracao_zendesk izd where izd.zendesk_ticket_key = :zendeskTicketKey')
		query.setInteger('zendeskTicketKey',zendeskTicketKey)
		return query.uniqueResult() as Integer
	}
	
	void inserirTicketKeyIntegradoComZendesk(int ticketKey, int zendeskTicketKey){
		def query = session.createSQLQuery(' INSERT INTO integracao_zendesk( ticket_key, zendesk_ticket_key ) VALUES (:ticketKey, :zendeskTicketKey) ')
		query.setInteger('ticketKey', ticketKey)
		query.setInteger('zendeskTicketKey', zendeskTicketKey)
		query.executeUpdate()
	}
	
	void excluirVinculoComZendesk(int ticketKey){
		def query = session.createSQLQuery('DELETE FROM integracao_zendesk WHERE ticket_key = :ticketKey')
		query.setInteger('ticketKey', ticketKey)
		query.executeUpdate()
	}
	
	void relacionarComZendesk(ticketKey, zendeskTicketKey) {
		def sql = 'insert into integracao_zendesk (ticket_key, zendesk_ticket_key) values (:ticketKey, :zendeskTicketKey)'
		def query = session.createSQLQuery(sql)
		query.setInteger 'ticketKey', ticketKey
		query.setInteger 'zendeskTicketKey', zendeskTicketKey
		query.executeUpdate()
	}
	
	public Integer obterNumeroDoTicketNoZendesk(Integer ticketKey) {
		def sql = 'select zendesk_ticket_key from integracao_zendesk izd where izd.ticket_key = :ticketKey'
		def query = session.createSQLQuery(sql)
		query.setInteger 'ticketKey', ticketKey
		return query.uniqueResult() as Integer
	}

	public Integer obterQuantidadeDeImpedimentosPorUsuario(String username) {
		def sql = 'select count(*) from ticket where responsavel_key = :username'
		def query = session.createSQLQuery(sql)
		query.setString 'username', username
		return query.uniqueResult() as Integer
	}

		
	public listarNotasDeRelease(Date dataInicial, Date dataFinal) {
		def sql = 'select ticket_key, titulo, data_de_pronto, notas_para_release from ticket t where t.data_de_pronto between :dataInicial and :dataFinal and notas_para_release is not null and char_length(notas_para_release) > 0'
		def query = session.createSQLQuery(sql)
		query.setDate 'dataInicial', dataInicial
		query.setDate 'dataFinal', dataFinal
		return query.list().collect { 
			new ReleaseNote(ticketKey:it[0] as Integer, titulo:it[1], dataDePronto: DateUtil.toDate(it[2]), notas:it[3])	
		}
	}
	
	public obterQuantidadeDeChecklistItemsNaoMarcadosPorTicket(int ticketKey) {
		def hql = 'select count(*) from ChecklistItem cli inner join cli.checklist cl where cl.ticket.ticketKey = :ticketKey and cli.marcado = false'
		return session.createQuery(hql).setInteger('ticketKey',ticketKey).uniqueResult() as Integer
	}
	
	def listarItensParaDashboard(){
		def sql = 
		"""select p.projeto_key, p.nome as projeto, 
			       b.backlog_key, b.descricao as backlog,
			       s.sprint_key, s.nome as sprint, 
			       ks.kanban_status_key, ks.descricao as etapa, ks.ordem,        
			       count(*)
			from ticket t
			left join projeto p on p.projeto_key = t.projeto_key
			left join backlog b on b.backlog_key = t.backlog_key
			left join sprint s on s.sprint_key = t.sprint
			full join kanban_status ks on ks.kanban_status_key = t.kanban_status_key
			where (t.sprint is null or s.fechado = false)
			and t.backlog_key != 4
			group by p.projeto_key, p.nome,          
			         s.sprint_key, s.nome, 
			         ks.kanban_status_key, 
			         ks.descricao, ks.ordem, b.backlog_key, b.descricao
			order by p.nome, b.descricao, s.nome, ks.ordem
		"""
		def query = session.createSQLQuery(sql)
		def mapa = [:]
		
		query.list().each {
			def projetoKey = it[0] as Integer
			def projeto = it[1]
			def backlogKey = it[2]
			def backlog = new Entry(it[2],it[3])
			def sprint = backlogKey == Backlog.SPRINT_BACKLOG ? new Entry(it[4],it[5]) : null
			def etapa = backlogKey == Backlog.SPRINT_BACKLOG ? new Entry(it[6],it[7]) : null
			def quantidade = it[9]

			def dashboardItem = mapa[projeto] ? mapa[projeto] : new DashboardItem(projetoKey:projetoKey, projeto:projeto)
			dashboardItem.mapaPorBacklogESprintEEtapa[backlog] = dashboardItem.mapaPorBacklogESprintEEtapa[backlog] ?: [:] 
			dashboardItem.mapaPorBacklogESprintEEtapa[backlog][sprint] = dashboardItem.mapaPorBacklogESprintEEtapa[backlog][sprint] ?: [:]
			dashboardItem.mapaPorBacklogESprintEEtapa[backlog][sprint][etapa] = (dashboardItem.mapaPorBacklogESprintEEtapa[backlog][sprint][etapa] ?: 0) + (quantidade as Integer)
			mapa[projeto] = dashboardItem 
		}
		
		sql = """
		select p.projeto_key, p.nome, 
			       tt.tipo_de_ticket_key, tt.descricao,			            
			       count(*)
        from ticket t
        left join projeto p on p.projeto_key = t.projeto_key
        left join tipo_de_ticket tt on tt.tipo_de_ticket_key = t.tipo_de_ticket_key
        where t.data_de_pronto is null and t.backlog_key != 4
        group by p.projeto_key, p.nome, tt.tipo_de_ticket_key, tt.descricao
		"""
		query = session.createSQLQuery(sql)
		query.list().each{
			def projetoKey = it[0] as Integer
			def projeto = it[1] 
			def tipoDeTicket = new Entry(it[2], it[3])
			def quantidade = it[4]
			
			def dashboardItem = mapa[projeto]
			dashboardItem.quantidadesPorTipoDeTicket[tipoDeTicket] = quantidade
		}
		
		
		sql = """
		select p.projeto_key, p.nome as projeto, m.milestone_key, m.nome, 
		       (100.0 * count(data_de_pronto) / count(*)) as percentual  
		from ticket t 
		inner join milestone m on t.milestone_key = m.milestone_key
		inner join projeto p on m.projeto_key = p.projeto_key
		group by p.projeto_key, p.nome, m.milestone_key, m.nome
		"""
		
		query = session.createSQLQuery(sql)
		query.list().each{
			def projetoKey = it[0] as Integer
			def projeto = it[1]
			def milestone = new Entry(it[2], it[3])
			def percentual = it[4]
			
			def dashboardItem = mapa[projeto]
			dashboardItem.percentualPorMilestone[milestone] = percentual
		}
		
		return mapa.values()
	}

		
}
