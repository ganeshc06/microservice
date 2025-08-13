package com.v2solutions.gateway_server.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimitConfig {

//    @Bean(name = "ipKeyResolver")
//    public KeyResolver ipKeyResolver() {
//        return exchange -> Mono.just(resolveIp(exchange));
//    }

    @Bean(name = "userOrIpKeyResolver")
    public KeyResolver userOrIpKeyResolver() {
        return exchange -> ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication())
                .filter(Authentication::isAuthenticated)
                .flatMap(auth -> principalKey(auth))
                .switchIfEmpty(Mono.just(resolveIp(exchange)));
    }

    private Mono<String> principalKey(Authentication auth) {
        Object p = auth.getPrincipal();
        if (p instanceof Jwt jwt) {
            String sub = jwt.getSubject();
            if (sub != null && !sub.isBlank()) return Mono.just(sub);
        }
        return Mono.justOrEmpty(auth.getName());
    }

    private String resolveIp(ServerWebExchange exchange) {
        String xff = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) return xff.split(",")[0].trim();
        var addr = exchange.getRequest().getRemoteAddress();
        return addr != null && addr.getAddress() != null ? addr.getAddress().getHostAddress() : "unknown";
    }
}
