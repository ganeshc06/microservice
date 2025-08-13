package com.v2solutions.user_service.error;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) { super(message); }
}