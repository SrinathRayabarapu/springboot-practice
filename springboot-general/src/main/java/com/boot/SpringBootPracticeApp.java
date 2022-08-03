package com.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class in spring boot. It initializes all the components, entities etc in spring.boot package.
 * 
 * @author Srinath.Rayabarapu
 */
@Slf4j
@SpringBootApplication
public class SpringBootPracticeApp {

	public static void main(String[] args) {
		log.info("SpringBoot started..");
		SpringApplication.run(SpringBootPracticeApp.class, args);
	}
	
}