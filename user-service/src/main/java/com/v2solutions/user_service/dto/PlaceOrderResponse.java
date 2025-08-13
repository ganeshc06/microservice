package com.v2solutions.user_service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PlaceOrderResponse(
        UUID orderId,
        String status,
        String message,
        BigDecimal totalAmount
) {}
