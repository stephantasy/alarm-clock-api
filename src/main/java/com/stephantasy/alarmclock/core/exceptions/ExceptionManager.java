package com.stephantasy.alarmclock.core.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;

@ControllerAdvice
public class ExceptionManager extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "This should be application specific";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }


    @ExceptionHandler(CustomHttpException.class)
    public ResponseEntity<HttpError> handleCustomHttpException(CustomHttpException ex) {
        return buildResponse(new HttpError(ex.getStatusCode(), ex.getMessage()));
    }


    /**
     * Builds the error response.
     * Extracts the http status from the error and forces the content type to 'application/json'
     *
     * @param httpError the http error
     * @return the error response
     */
    private ResponseEntity<HttpError> buildResponse(HttpError httpError) {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.put("Content-Type", Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));
        return new ResponseEntity<>(httpError, headers, HttpStatus.valueOf(httpError.getStatus()));
    }
}
