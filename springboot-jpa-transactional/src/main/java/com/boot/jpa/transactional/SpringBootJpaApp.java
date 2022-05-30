package com.boot.jpa.transactional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author Srinath.Rayabarapu
 */
@Slf4j
@SpringBootApplication
@EnableTransactionManagement
public class SpringBootJpaApp {

	public static void main(String[] args) {
		log.info("SpringBootJpaApp.main()");
		SpringApplication.run(SpringBootJpaApp.class, args);
	}
	
}