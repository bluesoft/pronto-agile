package br.com.bluesoft.pronto.to

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

class Entry {
	
	public Entry() {
		super()
	}
	
	public Entry(def key, def value) {
		super()
		this.key = key
		this.value = value
	}
	
	def key
	def value
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(key).append(value).toHashCode()
	}
	
	boolean equals(outro) {
		return new EqualsBuilder().append(key, outro.key).append(value, outro.value).isEquals()
	}
	
}
