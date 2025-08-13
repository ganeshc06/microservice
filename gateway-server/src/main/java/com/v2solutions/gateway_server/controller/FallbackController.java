package com.v2solutions.gateway_server.controller;

import com.v2solutions.gateway_server.error.ApiError;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
public class FallbackController {
    @GetMapping(value = "/fallback/books", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiError booksFallback() {
        return new ApiError("DOWNSTREAM_UNAVAILABLE", "Book Service is temporarily unavailable", 503, null, OffsetDateTime.now());
    }
    @GetMapping(value = "/fallback/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiError usersFallback() {
        return new ApiError("DOWNSTREAM_UNAVAILABLE", "User Service is temporarily unavailable", 503, null, OffsetDateTime.now());
    }
    @GetMapping(value = "/fallback/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiError ordersFallback() {
        return new ApiError("DOWNSTREAM_UNAVAILABLE", "Order Service is temporarily unavailable", 503, null, OffsetDateTime.now());
    }
}
