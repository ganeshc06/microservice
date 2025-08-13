package com.v2solutions.order_service.repository;

import com.v2solutions.order_service.domain.IdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKey, java.util.UUID> {
    Optional<IdempotencyKey> findByKey(String key);
}
