package br.com.bluesoft.pronto.model;

import java.sql.Blob;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Sprint {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int sprintKey;

	private String nome;

	private Date dataInicial;

	private Date dataFinal;

	private Blob imagem;

	public int getSprintKey() {
		return sprintKey;
	}

	public void setSprintKey(final int sprintKey) {
		this.sprintKey = sprintKey;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(final String nome) {
		this.nome = nome;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(final Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(final Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Blob getImagem() {
		return imagem;
	}

	public void setImagem(final Blob imagem) {
		this.imagem = imagem;
	}

	@Override
	public String toString() {
		return this.getNome();
	}
}
