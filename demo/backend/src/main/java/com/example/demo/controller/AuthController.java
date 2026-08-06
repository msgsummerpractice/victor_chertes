package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.MfaVerificationRequestDTO;
import com.example.demo.dto.SignInRequestDTO;
import com.example.demo.dto.SignInResponseDTO;
import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.service.AuthService;
import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.service.MfaService;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "https://mango-mushroom-0b29df703.7.azurestaticapps.net")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final UserService userService;
    private final MfaService mfaService;
    private final CustomUserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager, AuthService authService, 
                          UserService userService, MfaService mfaService, CustomUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.userService = userService;
        this.mfaService = mfaService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody SignInRequestDTO loginRequest) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                 loginRequest.getPassword()
                )
        );

        mfaService.generateAndSendOtp(loginRequest.getUsername());

        return ResponseEntity.ok(Map.of(
            "message", "MFA code generated successfully. Please check your console and verify at /auth/verify-mfa",
            "username", loginRequest.getUsername()
        ));
    }

    @PostMapping("/verify-mfa")
    public ResponseEntity<?> verifyMfa(@RequestBody MfaVerificationRequestDTO mfaRequest) {
        try {
            boolean isValid = mfaService.verifyOtp(mfaRequest.getUsername(), mfaRequest.getCode());

            if (!isValid) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired MFA code"));
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(mfaRequest.getUsername());
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = authService.generateToken(authentication);

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(new SignInResponseDTO(jwt, roles));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "A apărut o eroare la verificarea MFA: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRequestDTO registerRequest) {
        UserResponseDTO createdUser = userService.createUser(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

}
