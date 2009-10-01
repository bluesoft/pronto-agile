package br.com.bluesoft.pronto.model;

public enum Classificacao {

	ASCENDENTE("Ascendente"), DESCENDENTE("Descendente");

	private String descricao;

	private Classificacao(final String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
