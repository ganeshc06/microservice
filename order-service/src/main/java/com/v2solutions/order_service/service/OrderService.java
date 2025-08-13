package com.v2solutions.order_service.service;

import com.v2solutions.order_service.dto.CreateOrderRequest;
import com.v2solutions.order_service.dto.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderService {
    OrderResponse create(CreateOrderRequest request, String correlationId, String idempotencyKey);
    OrderResponse get(UUID id);
    Page<OrderResponse> list(UUID userId, Pageable pageable);
    OrderResponse cancel(UUID id, Long expectedVersion);
}
