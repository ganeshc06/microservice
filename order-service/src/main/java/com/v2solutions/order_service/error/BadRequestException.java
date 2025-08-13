package com.v2solutions.order_service.error;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) { super(message); }
}
