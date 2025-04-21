package com.smsapplication.Exceptions;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class MobileNumberExceptions extends Throwable {

    private HttpStatus statusCode;
    private String detailedMessage;

    public MobileNumberExceptions(HttpStatus code, String message, String detailedMessage) {
        super(message);
        this.statusCode = code;
        this.detailedMessage = detailedMessage;

    }
    public MobileNumberExceptions(HttpStatus code, String message) {
        super(message);
        this.statusCode = code;
    }


}
