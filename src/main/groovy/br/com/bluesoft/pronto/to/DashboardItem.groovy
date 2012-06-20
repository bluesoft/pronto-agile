package br.com.bluesoft.pronto.to

class DashboardItem {
	Integer projetoKey
	String projeto
	def mapaPorBacklogESprintEEtapa = [:]
	def quantidadesPorTipoDeTicket = [:]
	def percentualPorMilestone = [:]
	def pendencias = []
}

