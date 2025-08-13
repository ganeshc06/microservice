package com.v2solutions.gateway_server.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("gateway")
                .pathsToMatch("/fallback/**")
                .addOpenApiCustomizer(openApi -> openApi.setInfo(new Info().title("API Gateway").version("v1")))
                .build();
    }
}
