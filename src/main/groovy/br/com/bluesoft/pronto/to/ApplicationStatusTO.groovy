package br.com.bluesoft.pronto.to

import java.util.List;

class ApplicationStatusTO {
	def long currentTime;
	def long uptime;
	def long memHeapMax;
	def long memHeapCommited;
	def long memHeapFree;
	def long memNonHeapMax;
	def long memNonHeapCommited;
	def long memNonHeapFree;
	def long memPhysicalMax;
	def long memPhysicalUsed;
	def long cpuMax;
	def double cpuUsed;

	def dataSources;
	def webApps;

	String toJSON() {
		def result = "{ 'currentTime': ${currentTime}, 'uptime': ${uptime}, 'memHeapMax': ${memHeapMax}, 'memHeapCommited': ${memHeapCommited}, 'memHeapFree': ${memHeapFree}, 'memNonHeapMax': ${memNonHeapMax}, 'memNonHeapCommited': ${memNonHeapCommited}, 'memNonHeapFree': ${memNonHeapFree}, 'memPhysicalMax': ${memPhysicalMax}, 'memPhysicalUsed': ${memPhysicalUsed}, 'cpuMax': ${cpuMax}, 'cpuUsed': ${cpuUsed}, 'dataSources': ["
		
		if(dataSources) {
			dataSources.each({
				result += "$it, "
			})
			result = result[0..-2]
		}
		
		result += "], webapps: ["

		if (webApps) {
			webApps.each({
				result += "$it, "
			})
			result = result[0..-2]
		}

		result += "]}";
		return result;
	}

}
