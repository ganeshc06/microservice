package com.v2solutions.user_service.dto;

import jakarta.validation.constraints.*;

public record UserRequest(
        @NotBlank String name,
        @Email @NotBlank String email,
        @Pattern(regexp = "^[0-9+\\-() ]{7,20}$", message = "invalid mobile number") String mobile,
        @Size(max = 1000) String address
) {}
