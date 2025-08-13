package com.v2solutions.order_service.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor headerPropagationInterceptor() {
        return template -> {
            RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
            if (attrs instanceof ServletRequestAttributes sra) {
                HttpServletRequest req = sra.getRequest();
                copyHeader(req, template, "Authorization");
                copyHeader(req, template, "X-Correlation-Id");
                copyHeader(req, template, "Idempotency-Key");
            }
        };
    }

    private void copyHeader(HttpServletRequest req, feign.RequestTemplate template, String name) {
        String val = req.getHeader(name);
        if (val != null && !val.isBlank()) template.header(name, val);
    }

    @Bean
    public ErrorDecoder errorDecoder() { return new FeignErrorDecoder(); }

    @Bean
    public Logger.Level feignLoggerLevel() { return Logger.Level.BASIC; }
}
