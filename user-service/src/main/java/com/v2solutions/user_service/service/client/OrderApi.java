package com.v2solutions.user_service.service.client;


import com.v2solutions.user_service.config.FeignConfig;
import com.v2solutions.user_service.dto.OrderCreateRequest;
import com.v2solutions.user_service.dto.PlaceOrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "order-service", path = "/api/v1/orders", configuration = FeignConfig.class)
public interface OrderApi {
    @PostMapping
    PlaceOrderResponse create(@RequestBody OrderCreateRequest payload);
}
