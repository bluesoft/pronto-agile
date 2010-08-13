package br.com.bluesoft.pronto.web.servlet

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.sf.ehcache.CacheManager;

class StartupListener implements ServletContextListener {
	
	static String contextPath 
	
	public void contextInitialized(ServletContextEvent sce){
		String path = sce.getServletContext().getResource("/").getPath();
		this.contextPath = path.substring(0, path.lastIndexOf("/"));
		this.contextPath = this.contextPath.substring(contextPath.lastIndexOf("/"));
		CacheManager.create();
	}
	
	public void contextDestroyed(ServletContextEvent sce){}
	
}
