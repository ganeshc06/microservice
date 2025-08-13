package com.v2solutions.books.controller;

import com.v2solutions.books.dto.BookRequest;
import com.v2solutions.books.dto.BookResponse;
import com.v2solutions.books.dto.PageResponse;
import com.v2solutions.books.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookService service;
    public BookController(BookService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_books_write') or hasAuthority('ROLE_books_admin')")
    public BookResponse create(@Valid @RequestBody BookRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_books_read','ROLE_books_write','ROLE_books_admin')")
    public BookResponse get(@PathVariable UUID id) { return service.get(id); }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_books_read','ROLE_books_write','ROLE_books_admin')")
    public PageResponse<BookResponse> list(
            @RequestParam(required = false) String author,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "title,asc") String sort
    ) {
        String[] s = sort.split(",");
        Sort.Direction dir = (s.length > 1 && s[1].equalsIgnoreCase("desc")) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, s[0]));
        Page<BookResponse> p = service.list(author, pageable);
        return new PageResponse<>(p.getContent(), p.getNumber(), p.getSize(), p.getTotalElements(), p.getTotalPages());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_books_write') or hasAuthority('ROLE_books_admin')")
    public BookResponse update(
            @PathVariable UUID id,
            @RequestHeader(name = "If-Match", required = false) Long expectedVersion,
            @Valid @RequestBody BookRequest request) {
        return service.update(id, expectedVersion, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_books_admin')")
    public void delete(@PathVariable UUID id,
                       @RequestHeader(name = "If-Match", required = false) Long expectedVersion) {
        service.delete(id, expectedVersion);
    }
}
