package com.v2solutions.order_service.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UUID userId,
        String isbn,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalAmount,
        String status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        Long version
) {}
