package com.v2solutions.user_service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderCreateRequest(
        UUID userId,
        String isbn,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalAmount
) {}
