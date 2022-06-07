package com.controllers;

import com.services.ProblematicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestController
public class GlobalExceptionHandlerController {

    @Autowired
    private ProblematicService problematicService;

    @GetMapping(value = "/handleError")
    public ResponseEntity<String> raiseNPE(){
        log.info("errorHandling called");
        problematicService.raiseNPE();
        log.info("Returning from errorHandling");
        return new ResponseEntity<>("NPE raised", INTERNAL_SERVER_ERROR);
    }

}
