package com.v2solutions.user_service.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        String mobile,
        String address,
        String status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        Long version
) {}
