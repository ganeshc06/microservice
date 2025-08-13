package com.v2solutions.user_service.service.client;

import com.v2solutions.user_service.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "book-service", path = "/api/v1/books", configuration = FeignConfig.class)
public interface BookApi {
    @GetMapping(params = "isbn")
    BookSummary getByIsbn(@RequestParam("isbn") String isbn);

    record BookSummary(String isbn, String title, String author, BigDecimal price) {}
}
