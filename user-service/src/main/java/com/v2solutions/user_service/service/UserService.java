package com.v2solutions.user_service.service;

import com.v2solutions.user_service.dto.PlaceOrderRequest;
import com.v2solutions.user_service.dto.PlaceOrderResponse;
import com.v2solutions.user_service.dto.UserRequest;
import com.v2solutions.user_service.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    UserResponse create(UserRequest request);
    UserResponse get(UUID id);
    Page<UserResponse> list(String name, Pageable pageable);
    UserResponse update(UUID id, Long expectedVersion, UserRequest request);
    void delete(UUID id, Long expectedVersion);

    PlaceOrderResponse placeOrder(PlaceOrderRequest request, String correlationId, String idempotencyKey);
}
