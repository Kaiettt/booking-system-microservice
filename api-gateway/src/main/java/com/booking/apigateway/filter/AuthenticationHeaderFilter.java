package com.booking.apigateway.filter;

import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;

import java.util.Base64;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AuthenticationHeaderFilter implements GlobalFilter, Ordered {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (isPublicEndpoint(path)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String[] parts = token.split("\\.");
                if (parts.length == 3) {
                    String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
                    Map<String, Object> claims = objectMapper.readValue(payloadJson, Map.class);

                    String userId = (String) claims.get("sub");
                    String username = (String) claims.get("username");
                    String role = (String) claims.get("role");

                    // Forward headers to downstream services
                    ServerWebExchange modifiedExchange = exchange.mutate()
                            .request(r -> r.headers(headers -> {
                                headers.add("X-UserId", userId);
                                headers.add("X-Username", username);
                                headers.add("X-Role", role);
                            }))
                            .build();

                    return chain.filter(modifiedExchange);
                }
            } catch (Exception e) {
                e.printStackTrace(); // Just log if payload cannot be parsed
            }
        }

        return chain.filter(exchange);
    }

    private boolean isPublicEndpoint(String path) {
        return path.startsWith("/actuator") ||
                path.startsWith("/auth") ||
                path.equals("/error");
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
