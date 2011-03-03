package br.com.bluesoft.pronto.to

import org.junit.Test;

import spock.lang.Specification;

import static org.junit.Assert.*

class WebAppTOTest extends Specification {

	def "it should convert to string"() {
		given: "the value"
		def obj = new WebAppTO()
		obj.name = 'teste'
		obj.sessions = 10
		obj.processingTime = 1.5
		obj.version = '1.0'
		
		when: "converted to string"
		def result = "${obj}"
		
		then:
		result == "{ 'name': 'teste', 'sessions': 10, 'processingTime': 1, 'version': '1.0' }"
	}
}
