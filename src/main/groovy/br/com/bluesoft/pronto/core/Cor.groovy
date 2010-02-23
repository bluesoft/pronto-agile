package br.com.bluesoft.pronto.core

public enum Cor {
	
	VERMELHO("Vermelho"), 
	LARANJA("Laranja"), 
	AZUL("Azul"), 
	VERDE("Verde"), 
	ROSA("Rosa"), 
	PRETO("Preto"), 
	CINZA("Cinza")
	
	private String descricao;
	
	public String getDescricao() {
		return descricao;
	}
	
	private Cor(String descricao) {
		this.descricao = descricao
	}
	
}
