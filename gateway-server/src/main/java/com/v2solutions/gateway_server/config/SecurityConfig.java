package com.v2solutions.gateway_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(ex -> ex
                        .pathMatchers("/actuator/**", "/v3/api-docs/**", "/swagger-ui/**", "/fallback/**").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth -> oauth.jwt());

        return http.build();
    }

    /**
     * Reactive JWT decoder for WebFlux (Boot 3).
     * Prefers jwk-set-uri if configured, otherwise uses issuer-uri.
     */
//    @Bean
//    public ReactiveJwtDecoder reactiveJwtDecoder(Environment env) {
//        String jwk = env.getProperty("spring.security.oauth2.resourceserver.jwt.jwk-set-uri");
//        if (jwk != null && !jwk.isBlank()) {
//            return NimbusReactiveJwtDecoder.withJwkSetUri(jwk).build();
//        }
//        String issuer = env.getProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri");
//        if (issuer == null || issuer.isBlank()) {
//            throw new IllegalStateException("Either jwk-set-uri or issuer-uri must be configured for resource server JWT");
//        }
//        return ReactiveJwtDecoders.fromIssuerLocation(issuer);
//    }
}
