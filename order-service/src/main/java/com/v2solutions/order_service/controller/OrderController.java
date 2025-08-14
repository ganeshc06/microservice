package com.v2solutions.order_service.controller;

import com.v2solutions.order_service.dto.CreateOrderRequest;
import com.v2solutions.order_service.dto.OrderContactInfoDto;
import com.v2solutions.order_service.dto.OrderResponse;
import com.v2solutions.order_service.dto.PageResponse;
import com.v2solutions.order_service.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService service;
    private OrderContactInfoDto orderContactInfoDto;

    public OrderController(OrderService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ROLE_orders_write','ROLE_orders_admin')")
    public OrderResponse create(@Valid @RequestBody CreateOrderRequest request,
                                @RequestHeader(name = "X-Correlation-Id", required = false) String correlationId,
                                @RequestHeader(name = "Idempotency-Key", required = false) String idempotencyKey) {
        return service.create(request, correlationId, idempotencyKey);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_orders_read','ROLE_orders_write','ROLE_orders_admin')")
    public OrderResponse get(@PathVariable UUID id) { return service.get(id); }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_orders_read','ROLE_orders_write','ROLE_orders_admin')")
    public PageResponse<OrderResponse> list(
            @RequestParam(required = false) UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        String[] s = sort.split(",");
        Sort.Direction dir = (s.length > 1 && s[1].equalsIgnoreCase("desc")) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, s[0]));
        Page<OrderResponse> p = service.list(userId, pageable);
        return new PageResponse<>(p.getContent(), p.getNumber(), p.getSize(), p.getTotalElements(), p.getTotalPages());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_orders_admin')")
    public OrderResponse cancel(@PathVariable UUID id,
                                @RequestHeader(name = "If-Match", required = false) Long expectedVersion) {
        return service.cancel(id, expectedVersion);
    }

    @GetMapping("/contact-info")
    public ResponseEntity<OrderContactInfoDto> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderContactInfoDto);
    }
}
