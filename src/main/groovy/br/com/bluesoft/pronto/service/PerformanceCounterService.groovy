package br.com.bluesoft.pronto.service

import br.com.bluesoft.pronto.to.ApplicationStatusTO 
import br.com.bluesoft.pronto.util.JmxUtil;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.Set;

import javax.management.JMException;
import javax.management.ObjectName;

import org.springframework.stereotype.Service;

@Service
class PerformanceCounterService {
	
	def PerformanceCounterService() {
	}
	
	def preecheDadosDaMemoria(def to) throws JMException {
		MemoryUsage usage = ManagementFactory.memoryMXBean.heapMemoryUsage;
		to.memHeapMax = usage.max
		to.memHeapCommited = usage.committed
		to.memHeapFree = usage.committed - usage.used

		usage = ManagementFactory.memoryMXBean.nonHeapMemoryUsage;
		to.memNonHeapMax = usage.max
		to.memNonHeapCommited = usage.committed
		to.memNonHeapFree = usage.committed - usage.used

		Long valor = (Long) JmxUtil.getAttribute("java.lang:type=OperatingSystem", "TotalPhysicalMemorySize");
		if (valor)
			to.memPhysicalMax = valor;

		valor = (Long) JmxUtil.getAttribute("java.lang:type=OperatingSystem", "FreePhysicalMemorySize");
		if (valor)
			to.memPhysicalUsed = to.memPhysicalMax - valor
	}

	def preencheCurrentTime(def to) {
		to.setCurrentTime(System.currentTimeMillis());
	}

	def preecheDadosDaCpu(def to) {
		final RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
		final OperatingSystemMXBean system = ManagementFactory.getOperatingSystemMXBean();

		to.uptime = runtime.getUptime()
		to.cpuMax = system.getAvailableProcessors()
		to.cpuUsed = system.getSystemLoadAverage()
	}

	def obterStatsService() throws JMException {

		def lista = JmxUtil.queryNames("jboss:*");
		if (lista == null)
			return null;
			
		final boolean jboss = lista.size() > 0;
		
		def result
		if (jboss)
			result = new JBossAppStatsService();
		else
			result = new TomcatAppStatsService();
			
		return result
	}

	def criaStatus() throws JMException {
		def status = new ApplicationStatusTO();
		preencheCurrentTime(status);
		preecheDadosDaMemoria(status);
		preecheDadosDaCpu(status);
		def statsService = obterStatsService()
		if(statsService) {
			status.dataSources = statsService.listDataSources()
			status.webApps = statsService.listWebApps()
		}

		return status;
	}

}
