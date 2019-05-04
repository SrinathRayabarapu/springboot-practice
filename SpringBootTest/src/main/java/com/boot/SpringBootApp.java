package com.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class in spring boot. It initializes all the components, entities etc in
 * spring.boot package.
 * 
 * @author Srinath.Rayabarapu
 *
 */
@SpringBootApplication
public class SpringBootApp {
	private static final Logger LOG = LoggerFactory.getLogger("SpringBoot");
	
	public static void main(String[] args) {
		LOG.debug("SpringBoot started..");
		SpringApplication.run(SpringBootApp.class, args);
	}
	
}