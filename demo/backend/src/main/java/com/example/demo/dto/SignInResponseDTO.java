package com.example.demo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInResponseDTO {
    private String token;
    private List<String> roles;
}
