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

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import org.hibernate.EntityMode;
import org.hibernate.Session;
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

import br.com.bluesoft.pronto.annotations.Label;
import br.com.bluesoft.pronto.model.Ticket;
import br.com.bluesoft.pronto.model.TicketLog;
import br.com.bluesoft.pronto.service.Seguranca;
import br.com.bluesoft.pronto.util.DateUtil;

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
		return false;
	}

	@Override
	public final boolean onPreUpdate(final PreUpdateEvent event) {

		Session session = null;

		try {

			if (!event.getEntity().getClass().equals(Ticket.class)) {
				return false;
			}

			final Serializable entityId = event.getPersister().hasIdentifierProperty() ? event.getPersister().getIdentifier(event.getEntity(), event.getPersister().guessEntityMode(event.getEntity())) : null;
			final Date transTime = new Date();
			final EntityMode entityMode = event.getPersister().guessEntityMode(event.getEntity());
			Object oldPropValue = null;
			Object newPropValue = null;

			session = event.getPersister().getFactory().openSession();
			session.beginTransaction();

			final Object existingEntity = session.get(event.getEntity().getClass(), entityId);

			for (final String propertyName : event.getPersister().getPropertyNames()) {

				String campo = propertyName;
				final boolean temLabel = Ticket.class.getDeclaredField(propertyName).isAnnotationPresent(Label.class);
				if (temLabel) {
					campo = Ticket.class.getDeclaredField(propertyName).getAnnotation(Label.class).value();
				}

				newPropValue = event.getPersister().getPropertyValue(event.getEntity(), propertyName, entityMode);
				if (newPropValue != null) {
					final boolean ehUmaCollection = newPropValue instanceof Collection;
					if (!ehUmaCollection) {
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
								session.save(history);
							}
						}
					}
				}
			}

			session.getTransaction().commit();
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return false;
	}

	private String makeString(final Object x) {

		String str = String.valueOf(x);

		if (x instanceof Date) {
			str = DateUtil.toString((Date) x);
		}

		if (x == null || x.equals("null")) {
			return "em branco";
		} else {
			return str;
		}
	}

	@Override
	public final void onPreLoad(final PreLoadEvent event) {

	}
}
