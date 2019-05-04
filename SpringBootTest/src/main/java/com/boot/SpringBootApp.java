package com.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * main class to explore Spring Boot capabilities.
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