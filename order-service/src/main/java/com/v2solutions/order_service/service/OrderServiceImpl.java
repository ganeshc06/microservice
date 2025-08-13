package com.v2solutions.order_service.service;

import com.v2solutions.order_service.domain.IdempotencyKey;
import com.v2solutions.order_service.domain.Order;
import com.v2solutions.order_service.domain.OrderStatus;
import com.v2solutions.order_service.dto.CreateOrderRequest;
import com.v2solutions.order_service.dto.DownstreamModels;
import com.v2solutions.order_service.dto.OrderResponse;
import com.v2solutions.order_service.error.ConflictException;
import com.v2solutions.order_service.error.NotFoundException;
import com.v2solutions.order_service.repository.IdempotencyKeyRepository;
import com.v2solutions.order_service.repository.OrderRepository;
import com.v2solutions.order_service.service.client.BookApi;
import com.v2solutions.order_service.service.client.UserApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository repo;
    private final IdempotencyKeyRepository idemRepo;
    private final BookApi bookApi;
    private final UserApi userApi;

    public OrderServiceImpl(OrderRepository repo, IdempotencyKeyRepository idemRepo, BookApi bookApi, UserApi userApi) {
        this.repo = repo; this.idemRepo = idemRepo; this.bookApi = bookApi; this.userApi = userApi;
    }

    @Override
    public OrderResponse create(CreateOrderRequest request, String correlationId, String idempotencyKey) {
        // Idempotency: if key exists and has orderId, return that order
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            Optional<IdempotencyKey> existing = idemRepo.findByKey(idempotencyKey);
            if (existing.isPresent() && existing.get().getOrderId() != null) {
                return get(existing.get().getOrderId());
            }
            // pre-insert marker to prevent race
            if (existing.isEmpty()) {
                idemRepo.save(new IdempotencyKey(UUID.randomUUID(), idempotencyKey, null, OffsetDateTime.now()));
            }
        }

        // Validate user exists
        DownstreamModels.UserSummary user = userApi.getById(request.userId());
        if (user == null) throw new NotFoundException("User not found");

        // Fetch book and price
        DownstreamModels.BookSummary book = bookApi.getByIsbn(request.isbn());
        if (book == null) throw new NotFoundException("Book not found");

        int qty = Math.max(1, request.quantity());
        BigDecimal total = book.price().multiply(BigDecimal.valueOf(qty));
        Order order = new Order(UUID.randomUUID(), request.userId(), request.isbn(), qty, book.price(), total);
        Order saved = repo.save(order);

        // mark idempotency as completed
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            idemRepo.findByKey(idempotencyKey).ifPresent(k -> { k.setOrderId(saved.getId()); });
        }
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse get(UUID id) {
        Order o = repo.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
        return toResponse(o);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> list(UUID userId, Pageable pageable) {
        Page<Order> page = (userId == null) ? repo.findAll(pageable) : repo.findByUserId(userId, pageable);
        return page.map(this::toResponse);
    }

    @Override
    public OrderResponse cancel(UUID id, Long expectedVersion) {
        Order o = repo.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
        if (expectedVersion != null && !expectedVersion.equals(o.getVersion())) {
            throw new ConflictException("Version mismatch");
        }
        if (o.getStatus() == OrderStatus.CANCELLED) return toResponse(o);
        o.setStatus(OrderStatus.CANCELLED);
        return toResponse(o);
    }

    private OrderResponse toResponse(Order o) {
        return new OrderResponse(o.getId(), o.getUserId(), o.getIsbn(), o.getQuantity(), o.getUnitPrice(),
                o.getTotalAmount(), o.getStatus().name(), o.getCreatedAt(), o.getUpdatedAt(), o.getVersion());
    }
}
