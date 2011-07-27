package br.com.bluesoft.pronto.model

public enum TipoEstimativa {

	PMG("PMG - Pequeno, Médio ou Grande"), PLANNING_POKER("Planning Poker (Fibonacci)");
	
	private TipoEstimativa(String descricao) {
		this.descricao = descricao;
	}
	
	private String descricao;
	
	public String getDescricao() {
		return descricao;
	}
	
	public String getString() {
		return this.toString();
	}
	
}
