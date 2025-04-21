package com.smsapplication.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import com.google.common.collect.Lists;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDTO {
    private HttpStatus status;
    private String message;
    private Object details;
    private List<String> errors = Lists.newArrayList();

    public ErrorResponseDTO(final HttpStatus status, final String message, final List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors.addAll(errors);
    }

    public ErrorResponseDTO(final HttpStatus status, final String message, final String error) {
        this.status = status;
        this.message = message;
        this.errors.add(error);
    }

    public ErrorResponseDTO(final HttpStatus status, final String message) {
        this.status = status;
        this.message = message;
        if(HttpStatus.OK.value() != status.value()) {
            this.errors.add(message);
        }
    }

    public ErrorResponseDTO(HttpStatus status, String message, Object details) {
        super();
        this.status = status;
        this.message = message;
        this.details = details;
    }
}

