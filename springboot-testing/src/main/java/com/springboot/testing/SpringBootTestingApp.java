package com.springboot.testing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SpringBootTestingApp {

    public static void main(String[] args) {
        log.info("SpringBoot started..");
        SpringApplication.run(SpringBootTestingApp.class, args);
    }

}