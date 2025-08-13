package com.v2solutions.user_service.service;

import com.v2solutions.user_service.domain.User;
import com.v2solutions.user_service.dto.UserRequest;
import com.v2solutions.user_service.error.ConflictException;
import com.v2solutions.user_service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class UserServiceImplTest {
    @Test
    void create_conflict_on_duplicate_email() {
        var repo = Mockito.mock(UserRepository.class);
        Mockito.when(repo.findByEmailAndStatus(Mockito.eq("a@b.com"), any())).thenReturn(Optional.of(Mockito.mock(User.class)));
        var svc = new UserServiceImpl(repo, null, null);
        var req = new UserRequest("Name", "a@b.com", null, null);
        assertThrows(ConflictException.class, () -> svc.create(req));
    }
}
