package com.v2solutions.order_service.dto;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record CreateOrderRequest(
        @NotNull UUID userId,
        @NotBlank String isbn,
        @Min(1) int quantity
) {}
