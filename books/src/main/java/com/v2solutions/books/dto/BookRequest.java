package com.v2solutions.books.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookRequest(
        @NotBlank String title,
        @NotBlank String author,
        @NotBlank @Size(max = 20) String isbn,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal price,
        LocalDate publishedDate
) {}
