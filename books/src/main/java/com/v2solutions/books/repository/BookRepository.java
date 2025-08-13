package com.v2solutions.books.repository;

import com.v2solutions.books.domain.Book;
import com.v2solutions.books.domain.BookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    Optional<Book> findByIsbnAndStatus(String isbn, BookStatus status);
    Page<Book> findByAuthorContainingIgnoreCaseAndStatus(String author, BookStatus status, Pageable pageable);
}
