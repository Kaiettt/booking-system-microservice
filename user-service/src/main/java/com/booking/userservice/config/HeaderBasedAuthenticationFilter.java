    package com.booking.userservice.config;

    import jakarta.servlet.FilterChain;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.web.filter.OncePerRequestFilter;

    import java.io.IOException;
    import java.util.Collections;
    @Slf4j
    public class HeaderBasedAuthenticationFilter extends OncePerRequestFilter {

        private static final String HEADER_USER_ID = "X-User-Id";
        private static final String HEADER_USER_NAME = "X-User-Name";
        private static final String HEADER_USER_ROLE = "X-User-Role"; // single role

        @Override
        protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain filterChain) throws ServletException, IOException {

            String path = request.getRequestURI(); // path của request
            String userId = request.getHeader(HEADER_USER_ID);
            String username = request.getHeader(HEADER_USER_NAME);
            String role = request.getHeader(HEADER_USER_ROLE);

            if (userId != null && username != null && role != null && !role.isBlank()) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
                        role.startsWith("ROLE_") ? role : "ROLE_" + role);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, Collections.singletonList(authority));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                // SLF4J debug
                log.debug("Authenticated user: {}, role: {}, path: {}", username, role, path);
            } else {
                SecurityContextHolder.clearContext();
                log.debug("No authentication headers found, path: {}", path);
            }

            filterChain.doFilter(request, response);
        }

    }
