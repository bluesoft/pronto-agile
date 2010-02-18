package br.com.bluesoft.pronto.web.servlet

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

class StartupListener implements ServletContextListener {
	
	static String contextName 
	
	public void contextInitialized(ServletContextEvent sce){
		this.contextName = "/" + sce.servletContext.servletContextName
	}
	
	public void contextDestroyed(ServletContextEvent sce){}
	
}
