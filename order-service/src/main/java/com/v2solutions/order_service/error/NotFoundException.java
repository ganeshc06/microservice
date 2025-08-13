package com.v2solutions.order_service.error;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) { super(message); }
}
