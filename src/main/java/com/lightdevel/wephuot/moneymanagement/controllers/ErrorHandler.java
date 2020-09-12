package com.lightdevel.wephuot.moneymanagement.controllers;

import com.lightdevel.wephuot.moneymanagement.exceptions.Error;
import com.lightdevel.wephuot.moneymanagement.exceptions.UserNotInTripException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(UserNotInTripException.class)
    public ResponseEntity<Error> UserNotInTripExceptionHandler(UserNotInTripException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Error(e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Void> RuntimeExceptionHandler(RuntimeException e) {
        return ResponseEntity.badRequest().build();
    }
}
