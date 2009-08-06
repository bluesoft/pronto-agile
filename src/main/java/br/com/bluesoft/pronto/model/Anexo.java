package br.com.bluesoft.pronto.model;

import java.io.Serializable;

import org.apache.commons.lang.WordUtils;

import com.google.common.collect.ImmutableSet;

public class Anexo implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String nomeCompleto;
	private final String nomeDoArquivo;
	private final String nomeParaExibicao;
	private final String extensao;

	public Anexo(final String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
		nomeDoArquivo = this.nomeCompleto.substring(0, nomeCompleto.lastIndexOf('.'));
		extensao = this.nomeCompleto.substring(nomeCompleto.lastIndexOf('.') + 1);
		nomeParaExibicao = WordUtils.capitalizeFully(nomeDoArquivo.replace("_", " "));
	}

	public String getExtensao() {
		return extensao;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public String getNomeParaExibicao() {
		return nomeParaExibicao;
	}

	public String getNomeDoArquivo() {
		return nomeDoArquivo;
	}

	private static final ImmutableSet<String> imagens = ImmutableSet.of("jpg", "jpeg", "gif", "png", "bmp");

	public boolean isImagem() {
		return imagens.contains(getExtensao());
	}

	private static final ImmutableSet<String> planilhas = ImmutableSet.of("xls", "xmls", "odx");

	public boolean isPlanilha() {
		return planilhas.contains(getExtensao());
	}

	@Override
	public String toString() {
		return nomeCompleto;
	}

}
