package br.com.bluesoft.pronto.model;

public enum TicketOrdem {

	/**/CODIGO("Código"),
	/**/TITULO("Título"),
	/**/TIPO("Tipo"),
	/**/CLIENTE("Cliente"),
	/**/BACKLOG("Backlog"),
	/**/VALOR_DE_NEGOCIO("Valor de Negócio"),
	/**/ESFORCO("Esforço"),
	/**/STATUS("Status");

	private String descricao;

	private TicketOrdem(final String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
