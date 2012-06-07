package br.com.bluesoft.pronto

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.core.env.Environment
import org.springframework.social.connect.ConnectionFactoryLocator
import org.springframework.social.connect.UsersConnectionRepository
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository
import org.springframework.social.connect.support.ConnectionFactoryRegistry
import org.springframework.social.connect.web.ProviderSignInController
import org.springframework.social.twitter.connect.TwitterConnectionFactory
import org.springframework.context.annotation.Bean;

@Configurable
public class SpringConfig {

	@Autowired
	private Environment environment;

	@Bean
	public ProviderSignInController providerSignInController() {
		return new ProviderSignInController(connectionFactoryLocator(),usersConnectionRepository(), new SimpleSignInAdapter());
	}

	@Bean
	@Scope(value="singleton", proxyMode=ScopedProxyMode.INTERFACES)
	public ConnectionFactoryLocator connectionFactoryLocator() {
		ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();

		registry.addConnectionFactory(new FacebookConnectionFactory(
				environment.getProperty("facebook.clientId"),
				environment.getProperty("facebook.clientSecret")));

		registry.addConnectionFactory(new TwitterConnectionFactory(
				environment.getProperty("twitter.consumerKey"),
				environment.getProperty("twitter.consumerSecret")));

		return registry;
	}

	@Bean
	@Scope(value="singleton", proxyMode=ScopedProxyMode.INTERFACES)
	public UsersConnectionRepository usersConnectionRepository() {
		return new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator(), textEncryptor);
	}
}
