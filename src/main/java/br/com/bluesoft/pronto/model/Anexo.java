/*
 * Copyright 2009 Pronto Agile Project Management.
 *
 * This file is part of Pronto.
 *
 * Pronto is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Pronto is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Pronto. If not, see <http://www.gnu.org/licenses/>.
 *
 */

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
