package com.v2solutions.gateway_server.error;

import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.Map;

@Order(-2)
public class GlobalErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {
    public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes,
                                          WebProperties webProperties,
                                          ErrorProperties errorProperties,
                                          ApplicationContext applicationContext) {
        // Boot 3 constructor signature
        super(errorAttributes, webProperties.getResources(), errorProperties, applicationContext);
    }

    @Override
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> attributes = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        int status = (int) attributes.getOrDefault("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        ApiError body = new ApiError(
                (String) attributes.getOrDefault("error", "GATEWAY_ERROR"),
                (String) attributes.getOrDefault("message", "Unexpected error"),
                status,
                MDC.get("correlationId"),
                OffsetDateTime.now()
        );
        return ServerResponse.status(status).contentType(MediaType.APPLICATION_JSON).bodyValue(body);
    }
}
