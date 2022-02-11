package com.optily.api.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class ErrorHandler extends ResponseEntityExceptionHandler {

    private final ObjectMapper mapper;

    @ExceptionHandler(value
            = { CampaignException.class})
    protected ResponseEntity<Object> handleInternal(
            CampaignException ex, WebRequest request) throws JsonProcessingException {
        String bodyOfResponse = "This should be application specific";
        ApiError error = new ApiError(ex.getCode(), ex.getDescription());
        return new ResponseEntity<>(error,  new HttpHeaders(), HttpStatus.BAD_REQUEST.value());
    }
}
