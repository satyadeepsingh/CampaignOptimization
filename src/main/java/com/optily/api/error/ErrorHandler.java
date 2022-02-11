package com.optily.api.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = { CampaignException.class})
    protected ResponseEntity<Object> handleInternal(
            CampaignException ex, WebRequest request) {
        ApiError error = new ApiError(ex.getCode(), ex.getDescription());
        return new ResponseEntity<>(error,  new HttpHeaders(), HttpStatus.BAD_REQUEST.value());
    }
}
