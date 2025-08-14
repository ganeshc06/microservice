package com.v2solutions.user_service.dto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        String mobile,
        String address,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long version
) {}
