package com.v2solutions.books.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.ExternalDocumentation;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("books")
                .pathsToMatch("/api/v1/**")
                .addOpenApiCustomizer(openApi -> openApi.setInfo(new Info()
                                .title("Book Service API").version("v1").description("CRUD + search for books"))
                        //.setExternalDocs(new ExternalDocumentation().description("Gateway-routed service"))
                )
                .build();
    }
}
