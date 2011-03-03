package br.com.bluesoft.pronto.util

import java.util.ArrayList;
import java.util.Set;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;


class JmxUtil {
	def static MBeanServer getMBeanServer() {
		def mBeanServerList = MBeanServerFactory.findMBeanServer(null);
		if (mBeanServerList == null || mBeanServerList.size() == 0)
			return null;

		return mBeanServerList.get(0);
	}

	def static queryNames(def String query) throws JMException {
		def server = getMBeanServer();
		if (!server || !query)
			return null;

		def qName = new ObjectName(query);
		return server.queryNames(qName, null);
	}

	def static getAttribute(final String objectName, final String attributeName) throws JMException {
		def MBeanServer server = getMBeanServer();
		if (!server || !objectName || ! attributeName)
			return null;

		def qName = new ObjectName(objectName);
		if (server.queryNames(qName, null).size() == 0)
			return null;

		server.getMBeanInfo(qName);

		return server.getAttribute(qName, attributeName);
	}

}
