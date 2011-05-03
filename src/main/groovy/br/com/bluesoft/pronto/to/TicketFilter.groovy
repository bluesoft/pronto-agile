package br.com.bluesoft.pronto.to

import br.com.bluesoft.pronto.model.Classificacao;
import br.com.bluesoft.pronto.model.TicketOrdem;

class TicketFilter {

	String query
	Integer kanbanStatusKey
	Integer clienteKey
	String reporter
	String sprintNome
	Boolean ignorarLixeira
	Integer tipoDeTicketKey
	Integer sprintKey
	Integer backlogKey
	Integer categoriaKey
	Integer projetoKey
	Integer moduloKey

	Date dataInicialPronto
	Date dataFinalPronto

	Date dataInicialCriacao
	Date dataFinalCriacao

	TicketOrdem ordem
	Classificacao classificacao

	public Classificacao getClassificacao() {
		classificacao ? classificacao : Classificacao.ASCENDENTE
	} 
	
	public TicketOrdem getOrdem() {
		ordem ? ordem : TicketOrdem.TITULO
	}
	
	boolean temCriterios() {
		return ( 
		(query != null && query.length() > 0) ||
		(sprintNome != null && sprintNome.length() > 0) ||
		(reporter != null && reporter.length() > 0) ||
		(kanbanStatusKey != null && kanbanStatusKey > 0) ||
		(clienteKey != null && clienteKey > 0) ||
		(tipoDeTicketKey != null && tipoDeTicketKey > 0) ||
		(backlogKey != null && backlogKey > 0) ||
		(sprintKey != null && sprintKey > 0) ||
		(categoriaKey != null && categoriaKey > 0) ||
		(projetoKey != null && projetoKey > 0) ||
		(moduloKey != null && moduloKey > 0) ||
		dataInicialPronto           != null ||
		dataFinalPronto             != null ||
		dataInicialCriacao          != null ||
		dataFinalCriacao            != null) 
	}
}
