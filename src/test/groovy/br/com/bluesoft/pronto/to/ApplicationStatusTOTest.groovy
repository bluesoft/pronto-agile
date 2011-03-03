package br.com.bluesoft.pronto.to

import javax.xml.crypto.Data;

import org.junit.Test;

import spock.lang.Specification;


class ApplicationStatusTOTest extends Specification {
	
	def "it should print correctly"() {
		given: "the value"
		def obj = new ApplicationStatusTO(currentTime: 1024, uptime: 100, memHeapMax: 101, memHeapCommited: 102, memHeapFree: 103, memNonHeapMax: 200, memNonHeapCommited: 202, memNonHeapFree: 203, memPhysicalMax: 301, memPhysicalUsed: 302, cpuMax: 2, cpuUsed: 1.1)
		
		when: "converted to JSON"
		def result = "${obj.toJSON()}"
		
		then:
		"{ 'currentTime': 1024, 'uptime': 100, 'memHeapMax': 101, 'memHeapCommited': 102, 'memHeapFree': 103, 'memNonHeapMax': 200, 'memNonHeapCommited': 202, 'memNonHeapFree': 203, 'memPhysicalMax': 301, 'memPhysicalUsed': 302, 'cpuMax': 2, 'cpuUsed': 1.1, 'dataSources': [], webapps: []}" == result
	}
	
	def "it should print correctly with webapps and datasources"() {
		given: "the value"
		def webApps = [new WebAppTO(name: 'teste', sessions: 20, processingTime: 2000, version: '1.0')]
		def dataSources = [ new DataSourceTO(name: 'test', max: 20, active: 10, used: 2) ]
		def obj = new ApplicationStatusTO(currentTime: 1024, uptime: 100, memHeapMax: 101, memHeapCommited: 102, memHeapFree: 103, memNonHeapMax: 200, memNonHeapCommited: 202, memNonHeapFree: 203, memPhysicalMax: 301, memPhysicalUsed: 302, cpuMax: 2, cpuUsed: 1.1, webApps: webApps, dataSources: dataSources)
		
		when: "converted to JSON"
		def result = "${obj.toJSON()}"

		then:
		"{ 'currentTime': 1024, 'uptime': 100, 'memHeapMax': 101, 'memHeapCommited': 102, 'memHeapFree': 103, 'memNonHeapMax': 200, 'memNonHeapCommited': 202, 'memNonHeapFree': 203, 'memPhysicalMax': 301, 'memPhysicalUsed': 302, 'cpuMax': 2, 'cpuUsed': 1.1, 'dataSources': [{ 'name': 'test', 'max': 20, 'active': 10, 'used': 2 },], webapps: [{ 'name': 'teste', 'sessions': 20, 'processingTime': 2000, 'version': '1.0' },]}" == result
	}
}

