package br.com.bluesoft.pronto.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.management.JMException;
import javax.management.ObjectName;

import br.com.bluesoft.pronto.to.DataSourceTO;
import br.com.bluesoft.pronto.to.WebAppTO;
import br.com.bluesoft.pronto.util.JmxUtil;

class TomcatAppStatsService {

	def listDataSources() throws JMException {
		def result = []

		def qNames = JmxUtil.queryNames("Catalina:type=DataSource,*");

		for (final ObjectName qName : qNames) {
			def ds = new DataSourceTO();

			ds.name = qName.getKeyProperty("name")
			ds.max = JmxUtil.getAttribute(qName.toString(), "maxActive") as Integer
			ds.active = JmxUtil.getAttribute(qName.toString(), "numActive") as Integer
			ds.used = JmxUtil.getAttribute(qName.toString(), "numIdle") + ds.getActive() as Integer

			result.add(ds);
		}

		return result;
	}

	def listWebApps() throws JMException {
		def List<WebAppTO> result = []

		def qNames = JmxUtil.queryNames("Catalina:j2eeType=WebModule,*");
		
		if(!qNames)
			return result
		
		for(def qName: qNames) {
			def webApp = new WebAppTO();
			def qNameStr = qName.toString()
			
			def appName = JmxUtil.getAttribute(qNameStr, 'path')
			if (appName && !appName.equals("")) {
				webApp.name = appName
				webApp.processingTime = JmxUtil.getAttribute(qNameStr, "processingTime") as long

				def queryName = "Catalina:type=Manager,path=${appName},*" as String
				def names = JmxUtil.queryNames(queryName)
				def manager = names.iterator().next();

				if (manager != null)
					webApp.sessions = JmxUtil.getAttribute(manager.toString(), "activeSessions");
						
				result.add(webApp)
			}
		}
		return result;
	}
}