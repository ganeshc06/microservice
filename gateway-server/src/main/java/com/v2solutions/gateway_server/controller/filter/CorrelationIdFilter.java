package com.v2solutions.gateway_server.controller.filter;

import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CorrelationIdFilter implements GlobalFilter, Ordered {
    public static final String HEADER = "X-Correlation-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        String cid = exchange.getRequest().getHeaders().getFirst(HEADER);
        if (cid == null || cid.isBlank()) cid = UUID.randomUUID().toString();

        ServerHttpRequest mutated = exchange.getRequest().mutate().header(HEADER, cid).build();

        MDC.put("correlationId", cid);
        return chain.filter(exchange.mutate().request(mutated).build())
                .doFinally(signal -> MDC.remove("correlationId"));
    }

    @Override public int getOrder() { return Ordered.HIGHEST_PRECEDENCE; }
}
