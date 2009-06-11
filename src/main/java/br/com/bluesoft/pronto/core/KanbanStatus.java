package br.com.bluesoft.pronto.core;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
public class KanbanStatus {

	public static final int TO_DO = 1;
	public static final int DOING = 2;

	public static final int TESTING = 21;

	public static final int DONE = 100;

	@Id
	private int kanbanStatusKey;

	private String descricao;

	public KanbanStatus() {

	}

	public KanbanStatus(final int kanbanStatusKey) {
		super();
		this.kanbanStatusKey = kanbanStatusKey;
	}

	public KanbanStatus(final int kanbanStatusKey, final String descricao) {
		super();
		this.kanbanStatusKey = kanbanStatusKey;
		this.descricao = descricao;
	}

	public int getKanbanStatusKey() {
		return kanbanStatusKey;
	}

	public void setKanbanStatusKey(final int kanbanStatusKey) {
		this.kanbanStatusKey = kanbanStatusKey;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return descricao;
	}

}
