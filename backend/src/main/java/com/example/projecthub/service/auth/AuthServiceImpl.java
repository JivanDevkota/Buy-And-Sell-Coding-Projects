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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private static final String USERNAME_EXISTS = "Username is already taken";
    private static final String EMAIL_EXISTS = "Email is already taken";
    private static final String INVALID_CREDENTIALS = "Invalid username or password";
    private static final String REFRESH_TOKEN_INVALID = "Invalid or expired refresh token";
    private static final String REFRESH_TOKEN_EMPTY = "Refresh token cannot be null or empty";
    private static final String USER_NOT_FOUND = "User not found";
    private static final String ROLE_BUYER = "ROLE_BUYER";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            log.warn("Registration failed: Username already taken - {}", registerRequest.getUsername());
            throw new IllegalArgumentException(USERNAME_EXISTS);
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            log.warn("Registration failed: Email already taken - {}", registerRequest.getEmail());
            throw new IllegalArgumentException(EMAIL_EXISTS);
        }

        Role buyerRole = roleRepository.findByName(ROLE_BUYER);
        if (buyerRole == null) {
            log.error("Buyer role not found in database");
            throw new RuntimeException("Buyer role not found");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        roles.add(buyerRole);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getUsername());

        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getUsername());
        return buildAuthResponse(savedUser, userDetails);
    }

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
            String username = userDetails.getUsername();
            
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));

            log.info("User logged in successfully: {}", user.getUsername());
            return buildAuthResponse(user, userDetails);

        } catch (AuthenticationException e) {
            log.warn("Login failed for user: {}", loginRequest.getUsername());
            throw new IllegalArgumentException(INVALID_CREDENTIALS);
        }
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            log.warn("Refresh token is null or empty");
            throw new IllegalArgumentException(REFRESH_TOKEN_EMPTY);
        }

        String username = jwtUtils.extractUsername(refreshToken);
        if (username == null) {
            log.warn("Failed to extract username from refresh token");
            throw new IllegalArgumentException(REFRESH_TOKEN_INVALID);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtUtils.isRefreshTokenValid(refreshToken, userDetails)) {
            log.warn("Invalid refresh token for user: {}", username);
            throw new IllegalArgumentException(REFRESH_TOKEN_INVALID);
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));

        String newAccessToken = jwtUtils.generateAccessToken(userDetails);
        log.info("Token refreshed successfully for user: {}", username);

        return buildAuthResponse(user, userDetails, refreshToken, newAccessToken);
    }

    private AuthResponse buildAuthResponse(User user, UserDetails userDetails) {
        String accessToken = jwtUtils.generateAccessToken(userDetails);
        String refreshToken = jwtUtils.generateRefreshToken(userDetails);
        return buildAuthResponse(user, userDetails, refreshToken, accessToken);
    }

    private AuthResponse buildAuthResponse(User user, UserDetails userDetails, String refreshToken, String accessToken) {
        Set<String> roleNames = new HashSet<>();
        for (Role role : user.getRoles()) {
            roleNames.add(role.getName());
        }

        return new AuthResponse(
                accessToken,
                refreshToken,
                user.getUsername(),
                user.getId(),
                user.getEmail(),
                roleNames
        );
    }

}
