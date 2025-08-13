package com.v2solutions.order_service.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "idempotency_keys")
public class IdempotencyKey {
    @Id
    private UUID id;
    @Column(name = "idem_key", nullable = false, unique = true)
    private String key;
    @Column(name = "order_id")
    private UUID orderId;
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected IdempotencyKey() {}
    public IdempotencyKey(UUID id, String key, UUID orderId, OffsetDateTime createdAt) {
        this.id = id; this.key = key; this.orderId = orderId; this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public String getKey() { return key; }
    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
}
