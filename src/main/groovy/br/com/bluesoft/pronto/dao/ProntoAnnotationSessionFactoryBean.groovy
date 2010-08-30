package br.com.bluesoft.pronto.dao

import org.hibernate.HibernateException
import org.hibernate.cfg.AnnotationConfiguration
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean

public class ProntoAnnotationSessionFactoryBean extends AnnotationSessionFactoryBean {

	private String environment;

	public void setEnvironment(final String environment) {
		this.environment = environment;
	}

	public ProntoAnnotationSessionFactoryBean() {
		super();
	}

	@Override
	protected void postProcessAnnotationConfiguration(final AnnotationConfiguration config) throws HibernateException {
		setShowSql(config);
		super.postProcessAnnotationConfiguration(config);
	}

	private void setShowSql(final AnnotationConfiguration config) {
		config.setProperty("hibernate.show_sql", isDevelopment() ? "true" : "false");
		config.setProperty("hibernate.cache.use_structured_entries", isDevelopment() ? "true" : "false");
	}

	private boolean isDevelopment() {
		return environment != null && environment.equals("development");
	}

}
