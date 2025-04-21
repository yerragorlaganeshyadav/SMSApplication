package com.smsapplication.Exceptions;

import org.springframework.http.HttpStatus;

public class OTPExceptions extends Throwable{

    private HttpStatus httpStatus;
    private String message;

    public OTPExceptions(HttpStatus httpStatus, String message){
        super(message);
        this.httpStatus = httpStatus;
    }
}
