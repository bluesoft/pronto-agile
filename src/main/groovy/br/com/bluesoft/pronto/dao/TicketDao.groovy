package br.com.bluesoft.pronto.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.bluesoft.pronto.core.Backlog;
import br.com.bluesoft.pronto.core.KanbanStatus;
import br.com.bluesoft.pronto.core.TipoDeTicket;
import br.com.bluesoft.pronto.model.Classificacao;
import br.com.bluesoft.pronto.model.Sprint;
import br.com.bluesoft.pronto.model.Ticket;
import br.com.bluesoft.pronto.model.TicketOrdem;
import br.com.bluesoft.pronto.model.Usuario;
import br.com.bluesoft.pronto.service.Seguranca;

@Repository
public class TicketDao extends DaoHibernate {

	@Override
	public Ticket obter(final Integer ticketKey) {

		final StringBuilder hql = new StringBuilder();
		hql.append("select distinct t from Ticket t ");
		hql.append("left join fetch t.sprint ");
		hql.append("left join fetch t.pai ");
		hql.append("left join fetch t.tipoDeTicket ");
		hql.append("left join fetch t.backlog ");
		hql.append("left join fetch t.kanbanStatus ");
		hql.append("left join fetch t.reporter ");
		hql.append("where t.ticketKey = :ticketKey");

		return (Ticket) getSession().createQuery(hql.toString()).setInteger("ticketKey", ticketKey).uniqueResult();
	}

	public Ticket obterComDependecias(final Integer ticketKey) {

		final StringBuilder hql = new StringBuilder();
		hql.append("select distinct t from Ticket t ");
		hql.append("left join fetch t.sprint ");
		hql.append("left join fetch t.pai ");
		hql.append("left join fetch t.tipoDeTicket ");
		hql.append("left join fetch t.backlog ");
		hql.append("left join fetch t.kanbanStatus ");
		hql.append("left join fetch t.reporter ");
		hql.append("left join fetch t.filhos ");
		hql.append("left join t.desenvolvedores ");
		hql.append("left join t.comentarios ");
		hql.append("left join t.logs ");
		hql.append("left join t.testadores ");
		hql.append("where t.ticketKey = :ticketKey");

		return (Ticket) getSession().createQuery(hql.toString()).setInteger("ticketKey", ticketKey).uniqueResult();
	}

	public TicketDao() {
		super(Ticket.class);
	}

	@Override
	public void salvar(final Ticket... tickets) {
		prepararParaSalvar(tickets);
		super.salvar(tickets);
		defineValores(tickets);
		getSession().flush();
	}

	private void prepararParaSalvar(final Ticket... tickets) {
		for (final Ticket ticket : tickets) {
			if (ticket.getScript() != null && ticket.getScript().getScriptKey() == 0) {
				ticket.setScript(null);
			}
		}
	}

	private void defineValores(final Ticket... tickets) {

		for (final Ticket ticket : tickets) {
			
			getSession().lock(ticket, LockMode.NONE);
			
			ticket.dataDaUltimaAlteracao = new Date()

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

				final Ticket pai = ticket.getPai();

				if (pai.isLixo() || pai.isImpedido()) {
					ticket.setBacklog(pai.getBacklog());
				} else {
					if (!ticket.isLixo() && !ticket.isImpedido()) {
						ticket.setBacklog(pai.getBacklog());
					}
				}

				ticket.setSprint(pai.getSprint());
				ticket.setTipoDeTicket((TipoDeTicket) getSession().get(TipoDeTicket.class, TipoDeTicket.TAREFA));

				if (ticket.isDone() && pai.isTodosOsFilhosProntos()) {
					if (pai.getDataDePronto() == null) {
						pai.setDataDePronto(new Date());
						pai.setKanbanStatus((KanbanStatus) getSession().get(KanbanStatus.class, KanbanStatus.DONE));
						super.getSession().update(pai);
					}
				} else {
					if (pai.isEmAndamento()) {
						pai.setKanbanStatus((KanbanStatus) getSession().get(KanbanStatus.class, KanbanStatus.DOING));
					} else {
						pai.setKanbanStatus((KanbanStatus) getSession().get(KanbanStatus.class, KanbanStatus.TO_DO));
					}
					pai.setDataDePronto(null);
				}

			}

			// Tarefa nao tem valor de Negocio
			if (ticket.isTarefa()) {
				ticket.setValorDeNegocio(0);
			}

			// Grava sysdate na criação
			if (ticket.getDataDeCriacao() == null) {
				ticket.setDataDeCriacao(new Date());
			}

			// Se o status for pronto tem que ter data de pronto.
			if (ticket.getKanbanStatus().getKanbanStatusKey() == KanbanStatus.DONE && ticket.getDataDePronto() == null) {
				ticket.setDataDePronto(new Date());
			}

			getSession().saveOrUpdate(ticket);

		}

	}

	public List<Ticket> buscar(String busca, final Integer kanbanStatusKey, final Integer clienteKey, final TicketOrdem ordem, final Classificacao classificacao) {

		final StringBuilder hql = new StringBuilder();
		hql.append(" select distinct t from Ticket t   ");
		hql.append(" left join fetch t.sprint          ");
		hql.append(" left join fetch t.reporter        ");
		hql.append(" left join fetch t.tipoDeTicket as tipoDeTicket ");
		hql.append(" left join fetch t.backlog as b    ");
		hql.append(" left join fetch t.kanbanStatus as kanbanStatus ");
		hql.append(" left join fetch t.filhos          ");
		hql.append(" left join fetch t.cliente as cliente  ");
		hql.append(" where upper(t.titulo) like :query ");

		if (kanbanStatusKey != null) {
			if (kanbanStatusKey == -1) {
				hql.append(" and t.dataDePronto is null ");
			} else if (kanbanStatusKey > 0) {
				hql.append(" and t.kanbanStatus.kanbanStatusKey = :kanbanStatusKey ");
			}
		}

		if (clienteKey != null && clienteKey > 0) {
			hql.append(" and t.cliente.clienteKey = :clienteKey ");
		}

		hql.append(buildOrdem(ordem, classificacao));

		busca = busca == null ? "" : busca;

		final Query query = getSession().createQuery(hql.toString()).setString("query", '%' + busca.toUpperCase() + '%');

		if (kanbanStatusKey != null && kanbanStatusKey > 0) {
			query.setInteger("kanbanStatusKey", kanbanStatusKey);
		}

		if (clienteKey != null && clienteKey > 0) {
			query.setInteger("clienteKey", clienteKey);
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
		builder.append(" and t.backlog.backlogKey = :backlogKey");
		builder.append(" order by t.valorDeNegocio desc, t.esforco desc");

		return getSession().createQuery(builder.toString()).setInteger("sprintKey", sprintKey).setInteger("backlogKey", Backlog.SPRINT_BACKLOG).list();
	}

	public List<Ticket> listarNaoConcluidosPorSprint(final int sprintKey) {

		final List<Ticket> ticketsDoSprint = listarPorSprint(sprintKey);
		final List<Ticket> ticketsNaoConcluidos = new ArrayList<Ticket>();

		for (final Ticket ticket : ticketsDoSprint) {
			if (ticket.getKanbanStatus().getKanbanStatusKey() != KanbanStatus.DONE) {
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

	public List<Usuario> listarDesenvolvedoresDoTicket(final int ticketKey) {
		final String hql = "select t.desenvolvedores from Ticket t where t.ticketKey = :ticketKey";
		return getSession().createQuery(hql).setInteger("ticketKey", ticketKey).list();
	}

	public List<Usuario> listarTestadoresDoTicket(final int ticketKey) {
		final String hql = "select t.testadores from Ticket t where t.ticketKey = :ticketKey";
		return getSession().createQuery(hql).setInteger("ticketKey", ticketKey).list();
	}

	public List<Ticket> listarEstoriasEDefeitosDoProductBacklog() {
		return listarEstoriasEDefeitosPorBacklog(Backlog.PRODUCT_BACKLOG);
	}

	public List<Ticket> listarEstoriasEDefeitosPorBacklog(final int backlogKey) {
		final StringBuilder builder = new StringBuilder();
		builder.append(" select distinct t from Ticket t");
		builder.append(" left join fetch t.filhos f ");
		builder.append(" left join fetch t.pai p");
		builder.append(" left join fetch t.kanbanStatus ");
		builder.append(" left join fetch t.tipoDeTicket ");
		builder.append(" where t.backlog.backlogKey = :backlogKey");
		builder.append(" and t.tipoDeTicket.tipoDeTicketKey in (:tipos)");

		final List<Ticket> lista = getSession().createQuery(builder.toString()).setInteger("backlogKey", backlogKey).setParameterList("tipos", [ TipoDeTicket.ESTORIA, TipoDeTicket.DEFEITO, TipoDeTicket.IDEIA ]).list();
		
		final List<Comparator> comparators = new ArrayList<Comparator>();
		comparators.add(new ReverseComparator(new BeanComparator("valorDeNegocio")));
		comparators.add(new ReverseComparator(new BeanComparator("esforco")));
		comparators.add(new BeanComparator("ticketKey"));
		final ComparatorChain comparatorChain = new ComparatorChain(comparators);

		Collections.sort(lista, comparatorChain);

		return lista;
	}

	public List<Ticket> listarEstoriasEDefeitosPorSprint(final int sprintKey) {
		final StringBuilder builder = new StringBuilder();

		builder.append(" select distinct t from Ticket t");
		builder.append(" left join fetch t.filhos f ");
		builder.append(" left join fetch t.sprint ");
		builder.append(" left join fetch t.tipoDeTicket ");
		builder.append(" left join fetch t.backlog ");
		builder.append(" left join fetch t.kanbanStatus ");
		builder.append(" left join fetch t.reporter ");
		builder.append(" where t.sprint.sprintKey = :sprintKey");
		builder.append(" and t.tipoDeTicket.tipoDeTicketKey in (:tipos)");

		final List<Ticket> lista = getSession().createQuery(builder.toString()).setInteger("sprintKey", sprintKey).setParameterList("tipos", [ TipoDeTicket.ESTORIA, TipoDeTicket.DEFEITO ]).list();

		final List<Comparator> comparators = new ArrayList<Comparator>();
		comparators.add(new ReverseComparator(new BeanComparator("valorDeNegocio")));
		comparators.add(new ReverseComparator(new BeanComparator("esforco")));
		comparators.add(new BeanComparator("ticketKey"));
		final ComparatorChain comparatorChain = new ComparatorChain(comparators);

		Collections.sort(lista, comparatorChain);

		return lista;
	}

	public List<Ticket> listarTarefasEmBacklogsDiferentesDasEstoriasPorBacklog(final int backlogKey) {
		final StringBuilder builder = new StringBuilder();
		builder.append(" select distinct t from Ticket t");
		builder.append(" left join fetch t.filhos f ");
		builder.append(" left join fetch t.sprint ");
		builder.append(" left join fetch t.tipoDeTicket ");
		builder.append(" left join fetch t.backlog ");
		builder.append(" left join fetch t.kanbanStatus ");
		builder.append(" left join fetch t.reporter ");
		builder.append(" where t.backlog.backlogKey = :backlogKey");
		builder.append(" and t.tipoDeTicket.tipoDeTicketKey = :tipoTarefa");
		builder.append(" and t.pai.backlog.backlogKey != t.backlog.backlogKey");

		builder.append(" order by t.valorDeNegocio desc, t.esforco desc");
		return getSession().createQuery(builder.toString()).setInteger("backlogKey", backlogKey).setInteger("tipoTarefa", TipoDeTicket.TAREFA).list();
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

	public void moverParaSprintAtual(final List<Ticket> ticketsParaMover, final Sprint sprintAtual) {
		for (final Ticket ticket : ticketsParaMover) {
			ticket.setSprint(sprintAtual);
		}
	}

}
