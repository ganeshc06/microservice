package com.v2solutions.gateway_server.config;

import com.v2solutions.gateway_server.error.GlobalErrorWebExceptionHandler;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;

@Configuration
public class ErrorConfig {
    @Bean
    public DefaultErrorAttributes errorAttributes() { return new DefaultErrorAttributes(); }

    // Spring Boot 3: inject full WebProperties, not just Resources
    @Bean
    public WebProperties webProperties() { return new WebProperties(); }

    // Spring Boot 3: separate ErrorProperties bean
    @Bean
    public ErrorProperties errorProperties() { return new ErrorProperties(); }

    // Register the custom error handler and wire message readers/writers (required in WebFlux)
    @Bean
    public GlobalErrorWebExceptionHandler globalErrorWebExceptionHandler(DefaultErrorAttributes errorAttributes,
                                                                         WebProperties webProperties,
                                                                         ErrorProperties errorProperties,
                                                                         ApplicationContext applicationContext,
                                                                         ServerCodecConfigurer codecConfigurer) {
        GlobalErrorWebExceptionHandler handler = new GlobalErrorWebExceptionHandler(
                errorAttributes, webProperties, errorProperties, applicationContext);
        handler.setMessageWriters(codecConfigurer.getWriters());
        handler.setMessageReaders(codecConfigurer.getReaders());
        return handler;
    }
}
