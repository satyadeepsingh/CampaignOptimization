package com.optily.api.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = { CampaignException.class})
    protected ResponseEntity<Object> handleInternal(
            CampaignException ex, WebRequest request) {
        log.error("CampaignException occurred: {}", ex.getMessage(), ex);
        ApiError error = new ApiError(ex.getCode(), ex.getDescription());
        return new ResponseEntity<>(error,  new HttpHeaders(), HttpStatus.BAD_REQUEST.value());
    }
}
