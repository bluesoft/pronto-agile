package br.com.bluesoft.pronto.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.StatelessSession;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.Initializable;
import org.hibernate.event.PreDeleteEvent;
import org.hibernate.event.PreDeleteEventListener;
import org.hibernate.event.PreInsertEvent;
import org.hibernate.event.PreInsertEventListener;
import org.hibernate.event.PreLoadEvent;
import org.hibernate.event.PreLoadEventListener;
import org.hibernate.event.PreUpdateEvent;
import org.hibernate.event.PreUpdateEventListener;

import br.com.bluesoft.pronto.model.Label;
import br.com.bluesoft.pronto.model.Ticket;
import br.com.bluesoft.pronto.model.TicketLog;
import br.com.bluesoft.pronto.service.Seguranca;

public final class HibernateAuditLogListener implements PreDeleteEventListener, PreInsertEventListener, PreUpdateEventListener, PreLoadEventListener, Initializable {

	private static final long serialVersionUID = 1L;

	@Override
	public final void initialize(final Configuration cfg) {

	}

	@Override
	public final boolean onPreDelete(final PreDeleteEvent event) {

		return false;
	}

	@Override
	public final boolean onPreInsert(final PreInsertEvent event) {

		try {

			if (!event.getEntity().getClass().equals(Ticket.class)) {
				return false;
			}

			final Date transTime = new Date();
			final EntityMode entityMode = event.getPersister().guessEntityMode(event.getEntity());
			Object newPropValue = null;

			final StatelessSession session = event.getPersister().getFactory().openStatelessSession();
			session.beginTransaction();

			for (final String propertyName : event.getPersister().getPropertyNames()) {
				newPropValue = event.getPersister().getPropertyValue(event.getEntity(), propertyName, entityMode);
				if (newPropValue != null) {
					if (!(newPropValue instanceof Collection)) {
						final TicketLog history = new TicketLog();
						history.setTicket((Ticket) event.getEntity());
						history.setData(transTime);
						history.setUsuario(Seguranca.getUsuario().getUsername());
						history.setCampo(null);
						history.setValorAntigo(null);
						history.setValorNovo(String.valueOf(newPropValue));
						history.setOperacao(TicketLog.INCLUSAO);
						session.insert(history);
					}
				}
			}

			session.getTransaction().commit();
			session.close();
		} catch (final HibernateException e) {
		}
		return false;
	}

	@Override
	public final boolean onPreUpdate(final PreUpdateEvent event) {
		try {

			if (!event.getEntity().getClass().equals(Ticket.class)) {
				return false;
			}

			final Serializable entityId = event.getPersister().hasIdentifierProperty() ? event.getPersister().getIdentifier(event.getEntity(), event.getPersister().guessEntityMode(event.getEntity())) : null;
			final Date transTime = new Date();
			final EntityMode entityMode = event.getPersister().guessEntityMode(event.getEntity());
			Object oldPropValue = null;
			Object newPropValue = null;

			final StatelessSession session = event.getPersister().getFactory().openStatelessSession();
			session.beginTransaction();

			final Object existingEntity = session.get(event.getEntity().getClass(), entityId);

			for (final String propertyName : event.getPersister().getPropertyNames()) {

				String campo = propertyName;
				if (Ticket.class.getDeclaredField(propertyName).isAnnotationPresent(Label.class)) {
					campo = Ticket.class.getDeclaredField(propertyName).getAnnotation(Label.class).value();
				}

				newPropValue = event.getPersister().getPropertyValue(event.getEntity(), propertyName, entityMode);
				if (newPropValue != null) {
					if (!(newPropValue instanceof Collection)) {
						oldPropValue = event.getPersister().getPropertyValue(existingEntity, propertyName, entityMode);

						final String oldValue = makeString(oldPropValue);
						final String newValue = makeString(newPropValue);

						if (!oldValue.equals(newValue)) {
							final TicketLog history = new TicketLog();
							history.setTicket((Ticket) event.getEntity());
							history.setData(transTime);
							history.setUsuario(Seguranca.getUsuario().getUsername());
							history.setCampo(campo);
							history.setValorAntigo(oldValue);
							history.setValorNovo(newValue);
							history.setOperacao(TicketLog.ALTERACAO);
							if (history.isDiferente()) {
								session.insert(history);
							}
						}
					}
				}
			}

			session.getTransaction().commit();
			session.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private String makeString(final Object x) {
		final String str = String.valueOf(x);
		if (x.equals("null")) {
			return "em branco";
		} else {
			return str;
		}
	}

	@Override
	public final void onPreLoad(final PreLoadEvent event) {

	}
}
