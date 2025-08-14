package com.v2solutions.user_service.controller;

import com.v2solutions.user_service.dto.*;
import com.v2solutions.user_service.service.UserService;
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
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService service;
    private UsersContactInfoDto usersContactInfoDto;

    public UserController(UserService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_users_write') or hasAuthority('ROLE_users_admin')")
    public UserResponse create(@Valid @RequestBody UserRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_users_read','ROLE_users_write','ROLE_users_admin')")
    public UserResponse get(@PathVariable UUID id) { return service.get(id); }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_users_read','ROLE_users_write','ROLE_users_admin')")
    public PageResponse<UserResponse> list(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name,asc") String sort
    ) {
        String[] s = sort.split(",");
        Sort.Direction dir = (s.length > 1 && s[1].equalsIgnoreCase("desc")) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, s[0]));
        Page<UserResponse> p = service.list(name, pageable);
        return new PageResponse<>(p.getContent(), p.getNumber(), p.getSize(), p.getTotalElements(), p.getTotalPages());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_users_write') or hasAuthority('ROLE_users_admin')")
    public UserResponse update(
            @PathVariable UUID id,
            @RequestHeader(name = "If-Match", required = false) Long expectedVersion,
            @Valid @RequestBody UserRequest request) {
        return service.update(id, expectedVersion, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_users_admin')")
    public void delete(@PathVariable UUID id,
                       @RequestHeader(name = "If-Match", required = false) Long expectedVersion) {
        service.delete(id, expectedVersion);
    }

    // --- Order orchestration ---
    @PostMapping("/orders")
    @PreAuthorize("hasAnyAuthority('ROLE_users_write','ROLE_users_admin')")
    public PlaceOrderResponse placeOrder(@Valid @RequestBody PlaceOrderRequest request,
                                         @RequestHeader(name = "X-Correlation-Id", required = false) String correlationId,
                                         @RequestHeader(name = "Idempotency-Key", required = false) String idempotencyKey) {
        return service.placeOrder(request, correlationId, idempotencyKey);
    }

    @GetMapping("/contact-info")
    public ResponseEntity<UsersContactInfoDto> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(usersContactInfoDto);
    }
}
