package br.com.bluesoft.pronto.util

import org.junit.*
import static org.junit.Assert.*

class StringUtilTest {
	
	@Test
	void retiraAcentuacao() {
		
		assertEquals 'x', StringUtil.retiraAcentuacao('x')
		assertEquals 'Andre', StringUtil.retiraAcentuacao('André')
		assertEquals 'Descricao', StringUtil.retiraAcentuacao('Descrição')
		assertEquals 'titulo da tarefa', StringUtil.retiraAcentuacao('título da tarefa')
		
	}
	
}