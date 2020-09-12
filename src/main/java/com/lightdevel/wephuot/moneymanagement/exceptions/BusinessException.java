package com.lightdevel.wephuot.moneymanagement.exceptions;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public String getMessage() {
        return super.getMessage();
    }
}
