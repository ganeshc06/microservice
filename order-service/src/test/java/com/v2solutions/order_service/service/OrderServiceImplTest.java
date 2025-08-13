package com.v2solutions.order_service.service;

import com.v2solutions.order_service.domain.Order;
import com.v2solutions.order_service.dto.CreateOrderRequest;
import com.v2solutions.order_service.dto.DownstreamModels;
import com.v2solutions.order_service.repository.IdempotencyKeyRepository;
import com.v2solutions.order_service.repository.OrderRepository;
import com.v2solutions.order_service.service.client.BookApi;
import com.v2solutions.order_service.service.client.UserApi;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceImplTest {
    @Test
    void create_order_basic_flow() {
        var repo = Mockito.mock(OrderRepository.class);
        var idemRepo = Mockito.mock(IdempotencyKeyRepository.class);
        var bookApi = Mockito.mock(BookApi.class);
        var userApi = Mockito.mock(UserApi.class);

        Mockito.when(userApi.getById(Mockito.any())).thenReturn(new DownstreamModels.UserSummary(UUID.randomUUID(), "n", "e", null));
        Mockito.when(bookApi.getByIsbn("X")).thenReturn(new DownstreamModels.BookSummary("X", "t", "a", new BigDecimal("10.00")));
        Mockito.when(repo.save(Mockito.any(Order.class))).thenAnswer(a -> a.getArgument(0));

        var svc = new OrderServiceImpl(repo, idemRepo, bookApi, userApi);
        var resp = svc.create(new CreateOrderRequest(UUID.randomUUID(), "X", 2), null, null);
        assertEquals(new BigDecimal("20.00"), resp.totalAmount());
    }
}
