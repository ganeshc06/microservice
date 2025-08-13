package com.v2solutions.order_service.repository;

import com.v2solutions.order_service.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Page<Order> findByUserId(UUID userId, Pageable pageable);
}
