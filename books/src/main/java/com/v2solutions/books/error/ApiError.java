package com.v2solutions.books.error;

import java.time.OffsetDateTime;

public record ApiError(String code, String message, int status, String correlationId, OffsetDateTime timestamp) {}
