package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInRequestDTO {
    private String username;
    private String password;
}
