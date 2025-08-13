package com.v2solutions.books.service;

import com.v2solutions.books.domain.Book;
import com.v2solutions.books.dto.BookRequest;
import com.v2solutions.books.error.ConflictException;
import com.v2solutions.books.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

class BookServiceImplTest {
    @Test
    void create_conflict_on_duplicate_isbn() {
        var repo = Mockito.mock(BookRepository.class);
        Mockito.when(repo.findByIsbnAndStatus(Mockito.eq("X"), any())).thenReturn(Optional.of(Mockito.mock(Book.class)));
        var svc = new BookServiceImpl(repo);
        var req = new BookRequest("t","a","X", new BigDecimal("10.00"), LocalDate.now());
        assertThrows(ConflictException.class, () -> svc.create(req));
    }
}