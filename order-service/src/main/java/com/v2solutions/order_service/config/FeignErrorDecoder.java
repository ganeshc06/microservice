package com.v2solutions.order_service.config;

import com.v2solutions.order_service.error.BadRequestException;
import com.v2solutions.order_service.error.ConflictException;
import com.v2solutions.order_service.error.NotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        int status = response.status();
        return switch (status) {
            case 400 -> new BadRequestException("Downstream rejected request");
            case 404 -> new NotFoundException("Resource not found in downstream");
            case 409 -> new ConflictException("Conflict from downstream");
            default -> new RuntimeException("Downstream error: " + status);
        };
    }
}
