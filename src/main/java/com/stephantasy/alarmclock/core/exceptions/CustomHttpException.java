package com.stephantasy.alarmclock.core.exceptions;

public class CustomHttpException extends RuntimeException {
    private int statusCode;

    public CustomHttpException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
