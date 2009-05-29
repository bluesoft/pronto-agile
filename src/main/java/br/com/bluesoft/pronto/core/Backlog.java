package br.com.bluesoft.pronto.core;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Backlog {

	public static final int IDEIAS = 1;
	public static final int PRODUCT_BACKLOG = 2;
	public static final int SPRINT_BACKLOG = 3;
	public static final int LIXEIRA = 4;
	public static final int IMPEDIMENTOS = 5;

	public Backlog() {

	}

	public Backlog(int backlogKey) {
		this.backlogKey = backlogKey;
	}

	public Backlog(int backlogKey, String descricao) {
		this.backlogKey = backlogKey;
		this.descricao = descricao;
	}

	@Id
	private int backlogKey;

	private String descricao;

	public int getBacklogKey() {
		return backlogKey;
	}

	public void setBacklogKey(int backlogKey) {
		this.backlogKey = backlogKey;
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
