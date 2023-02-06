package com.springboot.profiles.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ProfileController {

    @Value("${env.custom.message}")
    private String envMessage;

    @GetMapping("/profiles/message")
    public String getEnvVariable(){
        return envMessage;
    }


}
