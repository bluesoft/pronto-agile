package br.com.bluesoft.pronto.core;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class KanbanStatus {

	public static final int TO_DO = 1;
	public static final int DOING = 2;

	public static final int TO_TEST = 20;
	public static final int TESTING = 21;

	public static final int DONE = 100;

	@Id
	private int kanbanStatusKey;

	private String descricao;

	public KanbanStatus() {

	}

	public KanbanStatus(int kanbanStatusKey) {
		super();
		this.kanbanStatusKey = kanbanStatusKey;
	}

	public KanbanStatus(int kanbanStatusKey, String descricao) {
		super();
		this.kanbanStatusKey = kanbanStatusKey;
		this.descricao = descricao;
	}

	public int getKanbanStatusKey() {
		return kanbanStatusKey;
	}

	public void setKanbanStatusKey(int kanbanStatusKey) {
		this.kanbanStatusKey = kanbanStatusKey;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return descricao;
	}
	
}
