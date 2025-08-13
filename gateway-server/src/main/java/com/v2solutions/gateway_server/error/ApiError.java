package com.v2solutions.gateway_server.error;

import java.time.OffsetDateTime;

public record ApiError(String code, String message, int status, String correlationId, OffsetDateTime timestamp) {}
