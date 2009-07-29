/*
 * Copyright 2009 Pronto Agile Project Management.
 *
 * This file is part of Pronto.
 *
 * Pronto is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Pronto is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Pronto. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package br.com.bluesoft.pronto.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.bluesoft.pronto.core.Backlog;
import br.com.bluesoft.pronto.core.KanbanStatus;
import br.com.bluesoft.pronto.core.TipoDeTicket;
import br.com.bluesoft.pronto.model.Ticket;
import br.com.bluesoft.pronto.model.Usuario;

@Repository
public class TicketDao extends DaoHibernate<Ticket, Integer> {

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

		for (final Ticket ticket : tickets) {

			if (ticket.getDataDeCriacao() == null) {
				ticket.setDataDeCriacao(new Date());
			}

			// Se um ticket tiver filhos, atualizar os dados dos filhos que devem ser sempre iguais aos do pai.
			if (ticket.temFilhos()) {
				ticket.setEsforco(ticket.getSomaDoEsforcoDosFilhos());

				for (final Ticket filho : ticket.getFilhos()) {
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
					pai.setKanbanStatus((KanbanStatus) getSession().get(KanbanStatus.class, KanbanStatus.TO_DO));
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

			getSession().saveOrUpdate(ticket);
		}

		getSession().flush();
	}

	@SuppressWarnings("unchecked")
	public List<Ticket> buscar(final String busca) {

		final StringBuilder hql = new StringBuilder();
		hql.append("from Ticket t                     ");
		hql.append("left join fetch t.sprint          ");
		hql.append("left join fetch t.reporter        ");
		hql.append("left join fetch t.tipoDeTicket    ");
		hql.append("left join fetch t.backlog         ");
		hql.append("left join fetch t.kanbanStatus    ");
		hql.append("left join fetch t.filhos          ");
		hql.append("where upper(t.titulo) like :query ");

		final Query query = getSession().createQuery(hql.toString()).setString("query", '%' + busca.toUpperCase() + '%');
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Ticket> listarPorSprint(final int sprintKey) {

		final StringBuilder builder = new StringBuilder();
		builder.append(" select distinct t from Ticket t");
		builder.append(" where t.sprint.sprintKey = :sprintKey");
		builder.append(" and t.backlog.backlogKey = :backlogKey");
		builder.append(" order by t.valorDeNegocio desc, t.esforco desc");

		return getSession().createQuery(builder.toString()).setInteger("sprintKey", sprintKey).setInteger("backlogKey", Backlog.SPRINT_BACKLOG).list();

	}

	@SuppressWarnings("unchecked")
	public List<Ticket> listarPorBacklog(final int backlogKey) {

		final StringBuilder builder = new StringBuilder();
		builder.append(" select distinct t from Ticket t");
		builder.append(" where t.backlog.backlogKey = :backlogKey");
		builder.append(" order by t.valorDeNegocio desc, t.esforco desc");

		return getSession().createQuery(builder.toString()).setInteger("backlogKey", backlogKey).list();

	}

	@SuppressWarnings("unchecked")
	public List<Usuario> listarDesenvolvedoresDoTicket(final int ticketKey) {
		final String hql = "select t.desenvolvedores from Ticket t where t.ticketKey = :ticketKey";
		return getSession().createQuery(hql).setInteger("ticketKey", ticketKey).list();
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> listarTestadoresDoTicket(final int ticketKey) {
		final String hql = "select t.testadores from Ticket t where t.ticketKey = :ticketKey";
		return getSession().createQuery(hql).setInteger("ticketKey", ticketKey).list();
	}

	public List<Ticket> listarEstoriasEDefeitosDoProductBacklog() {
		return listarEstoriasEDefeitosPorBacklog(Backlog.PRODUCT_BACKLOG);
	}

	@SuppressWarnings("unchecked")
	public List<Ticket> listarEstoriasEDefeitosPorBacklog(final int backlogKey) {
		final StringBuilder builder = new StringBuilder();
		builder.append(" select distinct t from Ticket t");
		builder.append(" left join fetch t.filhos f ");
		builder.append(" left join fetch t.pai p");
		builder.append(" left join fetch t.kanbanStatus ");
		builder.append(" left join fetch t.tipoDeTicket ");
		builder.append(" where t.backlog.backlogKey = :backlogKey");
		builder.append(" and t.tipoDeTicket.tipoDeTicketKey in (:tipos)");

		final List<Ticket> lista = getSession().createQuery(builder.toString()).setInteger("backlogKey", backlogKey).setParameterList("tipos", new Integer[] { TipoDeTicket.ESTORIA, TipoDeTicket.DEFEITO, TipoDeTicket.IDEIA }).list();

		final List<Comparator> comparators = new ArrayList<Comparator>();
		comparators.add(new ReverseComparator(new BeanComparator("valorDeNegocio")));
		comparators.add(new ReverseComparator(new BeanComparator("esforco")));
		comparators.add(new BeanComparator("ticketKey"));
		final ComparatorChain comparatorChain = new ComparatorChain(comparators);

		Collections.sort(lista, comparatorChain);

		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<Ticket> listarEstoriasEDefeitosPorSprint(final int sprintKey) {
		final StringBuilder builder = new StringBuilder();
		builder.append(" select distinct t from Ticket t");
		builder.append(" left join fetch t.filhos f ");
		builder.append(" left join fetch t.pai p");
		builder.append(" where t.sprint.sprintKey = :sprintKey");
		builder.append(" and t.tipoDeTicket.tipoDeTicketKey in (:tipos)");

		final List<Ticket> lista = getSession().createQuery(builder.toString()).setInteger("sprintKey", sprintKey).setParameterList("tipos", new Integer[] { TipoDeTicket.ESTORIA, TipoDeTicket.DEFEITO }).list();

		final List<Comparator> comparators = new ArrayList<Comparator>();
		comparators.add(new ReverseComparator(new BeanComparator("valorDeNegocio")));
		comparators.add(new ReverseComparator(new BeanComparator("esforco")));
		comparators.add(new BeanComparator("ticketKey"));
		final ComparatorChain comparatorChain = new ComparatorChain(comparators);

		Collections.sort(lista, comparatorChain);

		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<Ticket> listarTarefasEmBacklogsDiferentesDasEstoriasPorBacklog(final int backlogKey) {
		final StringBuilder builder = new StringBuilder();
		builder.append(" select distinct t from Ticket t");
		builder.append(" inner join fetch t.pai as pai ");
		builder.append(" where t.backlog.backlogKey = :backlogKey");
		builder.append(" and t.tipoDeTicket.tipoDeTicketKey = :tipoTarefa");
		builder.append(" and t.pai.backlog.backlogKey != t.backlog.backlogKey");

		builder.append(" order by t.valorDeNegocio desc, t.esforco desc");
		return getSession().createQuery(builder.toString()).setInteger("backlogKey", backlogKey).setInteger("tipoTarefa", TipoDeTicket.TAREFA).list();
	}

	@SuppressWarnings("unchecked")
	public List<Ticket> listarTicketsQueNaoEstaoNoBranchMaster() {
		final StringBuilder builder = new StringBuilder();
		builder.append(" select distinct t from Ticket t");
		builder.append(" left join fetch t.filhos f ");
		builder.append(" left join fetch t.pai p");
		builder.append(" where t.filhos is empty  and t.branch is not null and t.branch != 'master' and t.branch != ''");
		builder.append(" order by t.branch, t.titulo");
		return getSession().createQuery(builder.toString()).list();

	}

}
