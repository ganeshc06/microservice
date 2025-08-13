package com.v2solutions.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record PlaceOrderRequest(
        @NotBlank String isbn,
        @NotNull UUID userId,
        int quantity
) {}