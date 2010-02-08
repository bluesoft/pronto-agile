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

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class AnexoTest {

	@Test
	public void testNomes() {
		final Anexo anexo = new Anexo("ovo_de_pascoa.jpg");
		assertEquals(anexo.getNomeCompleto(), "ovo_de_pascoa.jpg");
		assertEquals(anexo.getNomeDoArquivo(), "ovo_de_pascoa");
		assertEquals(anexo.getExtensao(), "jpg");
		assertEquals(anexo.getNomeParaExibicao(), "Ovo De Pascoa");
	}

}
