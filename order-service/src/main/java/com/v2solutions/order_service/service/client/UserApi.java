package com.v2solutions.order_service.service.client;

import com.v2solutions.order_service.config.FeignConfig;
import com.v2solutions.order_service.dto.DownstreamModels;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service", path = "/api/v1/users", configuration = FeignConfig.class)
public interface UserApi {
    @GetMapping("/{id}")
    DownstreamModels.UserSummary getById(@PathVariable("id") UUID id);
}
