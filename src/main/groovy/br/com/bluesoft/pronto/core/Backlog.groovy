package br.com.bluesoft.pronto.core

import java.util.List

import javax.persistence.Cacheable;
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Transient

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.bluesoft.pronto.model.Ticket


@Entity
@Cacheable
@org.hibernate.annotations.Entity(mutable = false)
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "eternal")
public class Backlog {

	public static final int IDEIAS = 1
	public static final int PRODUCT_BACKLOG = 2
	public static final int SPRINT_BACKLOG = 3
	public static final int LIXEIRA = 4
	public static final int IMPEDIMENTOS = 5

	Backlog() {
	}

	Backlog(final int backlogKey) {
		this.backlogKey = backlogKey
	}

	Backlog(final int backlogKey, final String descricao) {
		this.backlogKey = backlogKey
		this.descricao = descricao
	}

	@Id
	int backlogKey

	String descricao
	
	String slug

	@OneToMany(mappedBy = "backlog")
	List<Ticket> tickets

	public String toString() {
		return descricao
	}

	public boolean isSprintBacklog() {
		return backlogKey == SPRINT_BACKLOG
	}

	public List<Ticket> getTickets() {
		return tickets
	}
	
}
