package com.v2solutions.books.controller;


import com.v2solutions.books.dto.BookResponse;
import com.v2solutions.books.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookController.class)
class BookControllerTest {
    @Autowired
    MockMvc mvc;
    @MockBean
    BookService service;

    @Test
    void get_unauthorized_without_jwt() throws Exception {
        when(service.get(UUID.randomUUID())).thenReturn((BookResponse) null);
        mvc.perform(get("/api/v1/books/" + UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
