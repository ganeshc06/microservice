package com.v2solutions.order_service.service.client;

import com.v2solutions.order_service.config.FeignConfig;
import com.v2solutions.order_service.dto.DownstreamModels;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "book-service", path = "/api/v1/books", configuration = FeignConfig.class)
public interface BookApi {
    @GetMapping(params = "isbn")
    DownstreamModels.BookSummary getByIsbn(@RequestParam("isbn") String isbn);
}
