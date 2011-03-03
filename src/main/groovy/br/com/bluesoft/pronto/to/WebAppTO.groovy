package br.com.bluesoft.pronto.to


class WebAppTO {

	def String name;
	def int sessions;
	def long processingTime;
	def String version;

	@Override
	String toString() {
		return "{ 'name': '${name}', 'sessions': ${sessions}, 'processingTime': ${processingTime}, 'version': '${version}' }"
	}
}
