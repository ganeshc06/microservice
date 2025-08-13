package com.v2solutions.books.error;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) { super(message); }
}