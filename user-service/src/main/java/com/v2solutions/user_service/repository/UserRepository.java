package com.v2solutions.user_service.repository;


import com.v2solutions.user_service.domain.User;
import com.v2solutions.user_service.domain.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmailAndStatus(String email, UserStatus status);
    Page<User> findByNameContainingIgnoreCaseAndStatus(String name, UserStatus status, Pageable pageable);
}
