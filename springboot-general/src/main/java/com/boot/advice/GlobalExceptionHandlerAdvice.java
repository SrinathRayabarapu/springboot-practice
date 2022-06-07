package com.boot.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException e){
        log.info("Global error handler called..");
        return new ResponseEntity<>("An error has occurred, please try again!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}