package com.springboot.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloChildController extends HelloController {

    @GetMapping(value = "/shubha")
    public String sayHi() {
        return "Hi..Shubha!, How Can I help you??";
    }

}
