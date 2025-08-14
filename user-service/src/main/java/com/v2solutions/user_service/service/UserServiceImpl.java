package com.v2solutions.user_service.service;


import com.v2solutions.user_service.domain.User;
import com.v2solutions.user_service.domain.UserStatus;
import com.v2solutions.user_service.dto.*;
import com.v2solutions.user_service.error.ConflictException;
import com.v2solutions.user_service.error.NotFoundException;
import com.v2solutions.user_service.repository.UserRepository;
import com.v2solutions.user_service.service.client.BookApi;
import com.v2solutions.user_service.service.client.OrderApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository repo;
    private final BookApi bookApi;
    private final OrderApi orderApi;

    public UserServiceImpl(UserRepository repo, BookApi bookApi, OrderApi orderApi) {
        this.repo = repo;
        this.bookApi = bookApi;
        this.orderApi = orderApi;
    }

    @Override
    public UserResponse create(UserRequest request) {
        repo.findByEmailAndStatus(request.email(), UserStatus.ACTIVE)
                .ifPresent(u -> {
                    throw new ConflictException("Email already exists");
                });

        User u = new User(UUID.randomUUID(), request.name(), request.email(), request.mobile(), request.address());
        User saved = repo.save(u);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse get(UUID id) {
        User u = repo.findById(id)
                .filter(x -> x.getStatus() == UserStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return toResponse(u);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> list(String name, Pageable pageable) {
        Page<User> page = (name == null || name.isBlank())
                ? repo.findAll(pageable)
                : repo.findByNameContainingIgnoreCaseAndStatus(name, UserStatus.ACTIVE, pageable);
        return page.map(this::toResponse);
    }

    @Override
    public UserResponse update(UUID id, Long expectedVersion, UserRequest request) {
        User u = repo.findById(id)
                .filter(x -> x.getStatus() == UserStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (expectedVersion != null && !expectedVersion.equals(u.getVersion())) {
            throw new ConflictException("Version mismatch");
        }

        if (!u.getEmail().equalsIgnoreCase(request.email())) {
            repo.findByEmailAndStatus(request.email(), UserStatus.ACTIVE)
                    .ifPresent(x -> { throw new ConflictException("Email already exists"); });
        }

        u.setName(request.name());
        u.setEmail(request.email());
        u.setMobile(request.mobile());
        u.setAddress(request.address());

        return toResponse(u);
    }

    @Override
    public void delete(UUID id, Long expectedVersion) {
        User u = repo.findById(id)
                .filter(x -> x.getStatus() == UserStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (expectedVersion != null && !expectedVersion.equals(u.getVersion())) {
            throw new ConflictException("Version mismatch");
        }

        u.setStatus(UserStatus.DELETED);
    }

    @Override
    public PlaceOrderResponse placeOrder(PlaceOrderRequest request, String correlationId, String idempotencyKey) {
        User u = repo.findById(request.userId())
                .filter(x -> x.getStatus() == UserStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("User not found"));

        var book = bookApi.getByIsbn(request.isbn());
        BigDecimal total = book.price().multiply(BigDecimal.valueOf(Math.max(1, request.quantity())));
        OrderCreateRequest payload = new OrderCreateRequest(
                u.getId(), book.isbn(), Math.max(1, request.quantity()), book.price(), total
        );
        return orderApi.create(payload);
    }

    private UserResponse toResponse(User u) {
        return new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getMobile(), u.getAddress(),
                u.getStatus().name(), u.getCreatedAt(), u.getUpdatedAt(), u.getVersion());
    }
}
