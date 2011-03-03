package br.com.bluesoft.pronto.to

import org.junit.Test;

import spock.lang.Specification;


class DataSourceTOTest extends Specification {

	def "it should print correctly"() {
		given: "the value"
		def obj = new DataSourceTO(name: "tst", max: 20, active: 10, used: 2)
		
		when: "converted to string"
		def result = "${obj}"
		
		then:
		"{ 'name': 'tst', 'max': 20, 'active': 10, 'used': 2 }" == result 
	}
}
