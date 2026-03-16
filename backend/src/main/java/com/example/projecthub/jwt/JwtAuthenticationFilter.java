package com.example.projecthub.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter
 * Intercepts requests and validates JWT tokens from Authorization header
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final int BEARER_LENGTH = 7;

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        try {
            String authHeader = request.getHeader(AUTHORIZATION_HEADER);
            
            if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(BEARER_LENGTH);
            String username = jwtUtils.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtils.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                        );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("JWT Token validated successfully for user: {}", username);
                } else {
                    log.warn("JWT Token validation failed for user: {} at endpoint: {}", username, request.getRequestURI());
                    response.sendError(401, "Invalid or expired token");
                    return;
                }
            }
            
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Error processing JWT token for endpoint {}: {}", request.getRequestURI(), e.getMessage());
            response.sendError(401, "Authentication failed: " + e.getMessage());
        }
    }
}
