package com.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
	private static final Logger LOG = LoggerFactory.getLogger("SpringBoot");
	
	public static void main(String[] args) {
		LOG.debug("SpringBoot started..");
		SpringApplication.run(App.class, args);
	}
	
}