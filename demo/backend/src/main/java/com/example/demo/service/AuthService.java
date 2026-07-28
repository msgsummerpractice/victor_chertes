package com.example.demo.service;

import org.springframework.security.core.Authentication;

public interface AuthService {
    String generateToken(Authentication authentication);
    String getUsernameFromToken(String token);
    boolean validateToken(String token);
}
