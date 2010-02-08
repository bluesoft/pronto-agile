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

package br.com.bluesoft.pronto.model

import java.io.Serializable

import org.apache.commons.lang.WordUtils

import com.google.common.collect.ImmutableSet

class Anexo implements Serializable {
	
	final String nomeCompleto
	final String nomeDoArquivo
	final String nomeParaExibicao
	final String extensao
	
	Anexo(final String nomeCompleto) {
		this.nomeCompleto = nomeCompleto
		nomeDoArquivo = this.nomeCompleto.substring(0, nomeCompleto.lastIndexOf('.'))
		extensao = this.nomeCompleto.substring(nomeCompleto.lastIndexOf('.') + 1)
		nomeParaExibicao = WordUtils.capitalizeFully(nomeDoArquivo.replace("_", " "))
	}
	
	static final ImmutableSet<String> imagens = ImmutableSet.of("jpg", "jpeg", "gif", "png", "bmp")
	
	boolean isImagem() {
		return imagens.contains(getExtensao())
	}
	
	static final ImmutableSet<String> planilhas = ImmutableSet.of("xls", "xmls", "odx")
	
	boolean isPlanilha() {
		return planilhas.contains(getExtensao())
	}
	
	@Override
	String toString() {
		return nomeCompleto
	}
	
}
