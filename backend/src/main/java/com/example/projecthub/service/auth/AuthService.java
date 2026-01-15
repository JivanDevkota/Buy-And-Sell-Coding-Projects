package com.example.projecthub.service.auth;

import com.example.projecthub.dto.user.AuthResponse;
import com.example.projecthub.dto.user.LoginRequest;
import com.example.projecthub.dto.user.RegisterRequest;

public interface AuthService {

    AuthResponse registerUser(RegisterRequest registerRequest);

    AuthResponse loginUser(LoginRequest loginRequest);
    AuthResponse refreshToken(String refreshToken);
}
