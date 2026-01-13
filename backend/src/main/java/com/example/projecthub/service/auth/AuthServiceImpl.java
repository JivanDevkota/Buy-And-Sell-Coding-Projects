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

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email is already taken");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        Role role = roleRepository.findByName("ROLE_SELLER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        User savedUser = userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getUsername());
        String accessToken = jwtUtils.generateAccessToken(userDetails);
        String refreshToken = jwtUtils.generateRefreshToken(userDetails);
        return new AuthResponse(accessToken, refreshToken, savedUser.getUsername(), savedUser.getId(), user.getEmail(),savedUser.getRoles().stream().map(Role::getName)
                .collect(Collectors.toSet()));
    }

    public AuthResponse loginUser(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        UserDetails userDetails=(UserDetails) authenticate.getPrincipal();
        String accessToken = jwtUtils.generateAccessToken(userDetails);
        String refreshToken = jwtUtils.generateRefreshToken(userDetails);

        User user=userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        return new AuthResponse(accessToken,refreshToken,user.getUsername(),user.getId(), user.getEmail(),user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
    }

}
