package com.boot.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloWorldController {

    @GetMapping(value = "/")
    public ResponseEntity<String> defaultRoute(){
        log.info("Default route called!");
        return new ResponseEntity<>("Welcome!", HttpStatus.OK);
    }

}
