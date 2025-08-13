package com.v2solutions.books.service;

import com.v2solutions.books.dto.BookRequest;
import com.v2solutions.books.dto.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BookService {
    BookResponse create(BookRequest request);
    BookResponse get(UUID id);
    Page<BookResponse> list(String author, Pageable pageable);
    BookResponse update(UUID id, Long expectedVersion, BookRequest request);
    void delete(UUID id, Long expectedVersion);
}
