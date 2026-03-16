package com.example.projecthub.service.auth;

import com.example.projecthub.dto.user.AuthResponse;
import com.example.projecthub.dto.user.LoginRequest;
import com.example.projecthub.dto.user.RegisterRequest;
import com.example.projecthub.jwt.JwtUtils;
import com.example.projecthub.model.Role;
import com.example.projecthub.model.User;
import com.example.projecthub.repository.RoleRepository;
import com.example.projecthub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Authentication Service Implementation
 * Handles user registration, login, and token refresh operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    /**
     * Register a new user with BUYER role by default
     * 
     * @param registerRequest contains username, email, password
     * @return AuthResponse with access token, refresh token, and user info
     * @throws RuntimeException if username or email already exists
     */
    @Override
    public AuthResponse registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            log.warn("Registration failed: Username already taken - {}", registerRequest.getUsername());
            throw new RuntimeException("Username is already taken");
        }
        
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            log.warn("Registration failed: Email already taken - {}", registerRequest.getEmail());
            throw new RuntimeException("Email is already taken");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        Role buyerRole = roleRepository.findByName("ROLE_BUYER");
        Set<Role> roles = new HashSet<>();
        roles.add(buyerRole);
        user.setRoles(roles);
        
        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getUsername());

        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getUsername());
        String accessToken = jwtUtils.generateAccessToken(userDetails);
        String refreshToken = jwtUtils.generateRefreshToken(userDetails);
        
        return new AuthResponse(
            accessToken, 
            refreshToken, 
            savedUser.getUsername(), 
            savedUser.getId(), 
            user.getEmail(),
            savedUser.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet())
        );
    }

    /**
     * Authenticate user with username and password
     * 
     * @param loginRequest contains username and password
     * @return AuthResponse with access token, refresh token, and user info
     * @throws UsernameNotFoundException if user is not found
     */
    @Override
    public AuthResponse loginUser(LoginRequest loginRequest) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), 
                    loginRequest.getPassword()
                )
            );
            
            UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
            String accessToken = jwtUtils.generateAccessToken(userDetails);
            String refreshToken = jwtUtils.generateRefreshToken(userDetails);

            User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

            log.info("User logged in successfully: {}", user.getUsername());
            
            return new AuthResponse(
                accessToken,
                refreshToken,
                user.getUsername(),
                user.getId(), 
                user.getEmail(),
                user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet())
            );
        } catch (Exception e) {
            log.error("Login failed for user: {}", loginRequest.getUsername(), e);
            throw new RuntimeException("Invalid credentials");
        }
    }

    /**
     * Refresh access token using a valid refresh token
     * 
     * @param refreshToken the refresh token from the request
     * @return AuthResponse with new access token and same refresh token
     * @throws RuntimeException if refresh token is invalid or expired
     */
    @Override
    public AuthResponse refreshToken(String refreshToken) {
        try {
            if (refreshToken == null || refreshToken.isEmpty()) {
                log.warn("Refresh token is null or empty");
                throw new RuntimeException("Refresh token cannot be null or empty");
            }

            String username = jwtUtils.extractUsername(refreshToken);
            
            if (username == null || username.isEmpty()) {
                log.warn("Failed to extract username from refresh token");
                throw new RuntimeException("Invalid refresh token: cannot extract username");
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (!jwtUtils.isRefreshTokenValid(refreshToken, userDetails)) {
                log.warn("Invalid refresh token for user: {}", username);
                throw new RuntimeException("Invalid or expired refresh token");
            }

            String newAccessToken = jwtUtils.generateAccessToken(userDetails);

            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

            log.info("Token refreshed successfully for user: {}", username);

            return new AuthResponse(
                newAccessToken,
                refreshToken,
                user.getUsername(),
                user.getId(),
                user.getEmail(),
                user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet())
            );
        } catch (Exception e) {
            log.error("Error refreshing token: {}", e.getMessage(), e);
            throw new RuntimeException("Error refreshing token: " + e.getMessage());
        }
    }

}
