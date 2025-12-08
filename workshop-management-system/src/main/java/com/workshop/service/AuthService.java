package com.workshop.service;

import com.workshop.dto.authentication.AuthRequest;
import com.workshop.dto.authentication.AuthResponse;
import com.workshop.dto.authentication.RegisterRequest;

public interface AuthService {

    AuthResponse login(AuthRequest request);

    AuthResponse register(RegisterRequest request);
}

