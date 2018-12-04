package com.springboot.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {
	
    @GetMapping(value = "/srinath")
	public String sayHi() {
        return "Hi..Srinath!, How Can I help you??";
	}

}