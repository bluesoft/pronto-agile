package br.com.bluesoft.pronto.core

public enum Cor {
	
	AMARELO("Amarelo"),
	AMARELO_BANANA("Amarelo Banana"),
	ASPARGO("Aspargo"),
	AZUL("Azul"),
	AZUL_OCEANO("Azul Oceano"),
	AZUL_SKY("Azul Sky"),
	BRANCO("Branco"),
	CINZA("Cinza"),
	LARANJA("Laranja"), 
	MOCA("Moca"),
	PRETO("Preto"), 
	ROSA("Rosa"),
	ROSA_CHOQUE("Rosa Choque"),
	VERDE("Verde"), 
	VERDE_AGUA("Verde Água"),
	VERDE_PRIMAVERA("Verde Primavera"),
	VERMELHO("Vermelho")
	
	private String descricao;
	
	public String getDescricao() {
		return descricao;
	}
	
	private Cor(String descricao) {
		this.descricao = descricao
	}
	
}
