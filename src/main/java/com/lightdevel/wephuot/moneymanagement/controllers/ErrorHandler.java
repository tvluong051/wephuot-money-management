package com.lightdevel.wephuot.moneymanagement.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Void> RuntimeExceptionHandler(RuntimeException e) {
        return ResponseEntity.badRequest().build();
    }
}
