package com.booking.apigateway.config;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Component
public class CustomAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    private static final String TOKEN_ERROR_MESSAGE = "Token is invalid or expired";

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        String timestamp = Instant.now().toString();
        String errorResponse = String.format(
                "{\"status\": 401, \"message\": \"%s\", \"timestamp\": \"%s\"}",
                TOKEN_ERROR_MESSAGE,
                timestamp
        );

        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        byte[] bytes = errorResponse.getBytes(StandardCharsets.UTF_8);

        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                .bufferFactory().wrap(bytes)));
    }
}

