package br.com.bluesoft.pronto.core

public enum Cor {
	
	AZUL("Azul"), 
	CINZA("Cinza"),
	LARANJA("Laranja"), 
	PRETO("Preto"), 
	ROSA("Rosa"), 
	VERDE("Verde"), 
	VERMELHO("Vermelho") 
	
	private String descricao;
	
	public String getDescricao() {
		return descricao;
	}
	
	private Cor(String descricao) {
		this.descricao = descricao
	}
	
}
