package com.springboot.proj.easynote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * main class for easy note taking application
 * 
 * @author Srinath.Rayabarapu
 */
@SpringBootApplication
@EnableJpaAuditing // enables to populate created and updated note model columns
public class EasyNotesApplication {

	private static final Logger LOG = LoggerFactory.getLogger("SpringBoot");

	public static void main(String[] args) {
		SpringApplication.run(EasyNotesApplication.class, args);
	}

}
