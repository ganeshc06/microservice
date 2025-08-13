package com.v2solutions.order_service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class DownstreamModels {
    public record BookSummary(String isbn, String title, String author, BigDecimal price) {}
    public record UserSummary(UUID id, String name, String email, String mobile) {}
}
