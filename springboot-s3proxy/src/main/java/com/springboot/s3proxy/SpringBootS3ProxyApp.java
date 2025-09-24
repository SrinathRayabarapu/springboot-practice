package com.springboot.s3proxy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring profiles are implemented for different environments. <p>
 *
 * Use the jvm parameter : -Dspring.profiles.active=dev
 *
 * Tutorial : https://www.baeldung.com/java-s3proxy-library
 */
@Slf4j
@SpringBootApplication
public class SpringBootS3ProxyApp {

    public static void main(String[] args) {
        log.info("SpringBootS3ProxyApp is started!");
        SpringApplication.run(SpringBootS3ProxyApp.class, args);
    }

}