package br.com.bluesoft.pronto.to

class DataSourceTO {
	def String name;
	def int max;
	def int active;
	def int used;

	String toString() {
		return "{ 'name': '${name}', 'max': ${max}, 'active': ${active}, 'used': ${used} }"
	}

}
