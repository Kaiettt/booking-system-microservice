package com.booking.userservice.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.booking.userservice.dto.response.LoginResponse;
import com.booking.userservice.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class SecurityUtil {

    private static long jwtAccessTokenExpiration;
    private static long jwtRefreshTokenExpiration;
    private static JwtEncoder jwtEncoder;

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    @Value("${security.jwt.access-token-validity-in-seconds}")
    private long accessTokenValidityConfig;

    @Value("${security.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityConfig;

    private final JwtEncoder encoder;

    public SecurityUtil(JwtEncoder jwtEncoder) {
        SecurityUtil.jwtEncoder = jwtEncoder;
        this.encoder = jwtEncoder;
    }

    @PostConstruct
    public void initStaticValues() {
        SecurityUtil.jwtAccessTokenExpiration = accessTokenValidityConfig;
        SecurityUtil.jwtRefreshTokenExpiration = refreshTokenValidityConfig;
    }

    public static String createToken(String userName, User user) {
        Instant now = Instant.now();
        Instant validity = now.plus(jwtAccessTokenExpiration, ChronoUnit.SECONDS);

        List<String> authorities = new ArrayList<>();
        authorities.add(user.getRole().name());

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(userName)
                .claim("authorities", authorities)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    public static String createRefreshToken(String userName, LoginResponse.UserLogin user) {
        Instant now = Instant.now();
        Instant validity = now.plus(jwtRefreshTokenExpiration, ChronoUnit.SECONDS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(userName)
                .claim("user", user)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .filter(authentication -> authentication.getCredentials() instanceof String)
                .map(authentication -> (String) authentication.getCredentials());
    }

    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }

    public static ResponseCookie CreateCookieForRefreshToken(String refreshToken){
        return ResponseCookie.from("refresh-token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(jwtRefreshTokenExpiration)
                .build();
    }
}
