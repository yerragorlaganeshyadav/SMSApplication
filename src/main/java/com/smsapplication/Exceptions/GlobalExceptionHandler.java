package com.smsapplication.Exceptions;

import com.smsapplication.ResponseDTO.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MobileNumberExceptions.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleInvalidMobileNumberException(MobileNumberExceptions ex) {
        return new ErrorResponseDTO(HttpStatus.BAD_REQUEST, ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(OTPExceptions.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleOTPVerificationExceptions(OTPExceptions ex){
        return new ErrorResponseDTO(HttpStatus.BAD_REQUEST, ex.getMessage(), LocalDateTime.now());
    }
}
