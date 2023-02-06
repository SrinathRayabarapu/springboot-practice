package com.springboot.profiles;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring profiles are implemented for different environments. <p>
 *
 * Use the jvm parameter : -Dspring.profiles.active=dev
 */
@Slf4j
@SpringBootApplication
public class SpringBootProfilesApp {

    public static void main(String[] args) {
        log.info("SpringBootProfilesApp is started...");
        SpringApplication.run(SpringBootProfilesApp.class, args);
    }

}