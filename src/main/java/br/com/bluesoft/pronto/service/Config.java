package br.com.bluesoft.pronto.service;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

@Service
public class Config {

	private Properties properties;

	@PostConstruct
	public void initialize() throws Exception {
		properties = new Properties();
		properties.load(Config.class.getResourceAsStream("/pronto.properties"));
	}

	public String getImagesFolder() {
		return properties.get("pronto.imagesFolder").toString();
	}

}
