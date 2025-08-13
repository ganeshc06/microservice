package com.v2solutions.user_service.error;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) { super(message); }
}
