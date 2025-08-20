package com.booking.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allow origins (adjust depending on your frontend/gateway)
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",   // local frontend
                "http://localhost:8080",   // API gateway
                "https://yourdomain.com"   // production
        ));

        // Allow credentials if you need cookies / auth headers
        configuration.setAllowCredentials(true);

        // Allow headers & methods
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization", "Cache-Control", "Content-Type",
                "X-User-Id", "X-User-Name", "X-User-Roles"
        ));
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        // Apply this config for all paths
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
