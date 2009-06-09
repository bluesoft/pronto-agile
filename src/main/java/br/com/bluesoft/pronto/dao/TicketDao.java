package br.com.bluesoft.pronto.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.bluesoft.pronto.core.Backlog;
import br.com.bluesoft.pronto.model.Ticket;
import br.com.bluesoft.pronto.model.Usuario;

@Repository
public class TicketDao extends DaoHibernate<Ticket, Integer> {

	public TicketDao() {
		super(Ticket.class);
	}

	@SuppressWarnings("unchecked")
	public List<Ticket> buscar(final String busca) {
		final String hql = "from Ticket t where upper(t.titulo) like :query";
		final Query query = getSession().createQuery(hql).setString("query", '%' + busca.toUpperCase() + '%');
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

}
