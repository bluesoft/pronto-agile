package br.com.bluesoft.pronto.util

import static org.junit.Assert.*

import org.junit.Test

class DateUtilTest {
	
	@Test
	void toDateDeveRetornarNullSeOParametroForNull() {
		assertNull DateUtil.toDate(null)
	}
	
	@Test
	void toDateDeveRetornarNullSeADataForInvalida() {
		assertNull DateUtil.toDate('ovo de pascoa')
	}
	
}	
