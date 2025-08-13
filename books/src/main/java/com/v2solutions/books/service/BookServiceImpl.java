package com.v2solutions.books.service;

import com.v2solutions.books.domain.Book;
import com.v2solutions.books.domain.BookStatus;
import com.v2solutions.books.dto.BookRequest;
import com.v2solutions.books.dto.BookResponse;
import com.v2solutions.books.error.ConflictException;
import com.v2solutions.books.error.NotFoundException;
import com.v2solutions.books.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class BookServiceImpl implements BookService {
    private final BookRepository repo;

    public BookServiceImpl(BookRepository repo) {
        this.repo = repo;
    }

    @Override
    public BookResponse create(BookRequest request) {
        // ISBN unique check among ACTIVE books
        repo.findByIsbnAndStatus(request.isbn(), BookStatus.ACTIVE)
                .ifPresent(b -> {
                    throw new ConflictException("ISBN already exists");
                });

        Book b = new Book(UUID.randomUUID(), request.title(), request.author(), request.isbn(),
                request.price(), request.publishedDate());
        Book saved = repo.save(b);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse get(UUID id) {
        Book b = repo.findById(id)
                .filter(book -> book.getStatus() == BookStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Book not found"));
        return toResponse(b);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookResponse> list(String author, Pageable pageable) {
        Page<Book> page = (author == null || author.isBlank())
                ? repo.findAll(pageable).map(b -> b)
                : repo.findByAuthorContainingIgnoreCaseAndStatus(author, BookStatus.ACTIVE, pageable);

        return (Page<BookResponse>) page
                .filter(b -> b.getStatus() == BookStatus.ACTIVE)
                .map(this::toResponse);
    }

    @Override
    public BookResponse update(UUID id, Long expectedVersion, BookRequest request) {
        Book b = repo.findById(id)
                .filter(book -> book.getStatus() == BookStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        if (expectedVersion != null && !expectedVersion.equals(b.getVersion())) {
            throw new ConflictException("Version mismatch");
        }

        // ISBN update uniqueness (if changed)
        if (!b.getIsbn().equals(request.isbn())) {
            repo.findByIsbnAndStatus(request.isbn(), BookStatus.ACTIVE)
                    .ifPresent(x -> {
                        throw new ConflictException("ISBN already exists");
                    });
        }

        b.setTitle(request.title());
        b.setAuthor(request.author());
        b.setIsbn(request.isbn());
        b.setPrice(request.price());
        b.setPublishedDate(request.publishedDate());

        return toResponse(b);
    }

    @Override
    public void delete(UUID id, Long expectedVersion) {
        Book b = repo.findById(id)
                .filter(book -> book.getStatus() == BookStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Book not found"));

    }

    private BookResponse toResponse(Book b) {
        return new BookResponse(
                b.getId(), b.getTitle(), b.getAuthor(), b.getIsbn(), b.getPrice(), b.getPublishedDate(),
                b.getStatus().name(), b.getCreatedAt(), b.getUpdatedAt(), b.getVersion()
        );
    }
}
