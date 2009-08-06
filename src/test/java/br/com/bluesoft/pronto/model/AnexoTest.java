package br.com.bluesoft.pronto.model;

import static junit.framework.Assert.*;

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
