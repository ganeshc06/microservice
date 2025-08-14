package com.v2solutions.books.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

public record BookResponse(
        UUID id,
        String title,
        String author,
        String isbn,
        BigDecimal price,
        LocalDate publishedDate,
        String status,
        LocalDateTime createdAt,
        LocalDateTime  updatedAt,
        Long version
) {}
