package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MfaVerificationRequestDTO {
    private String username;
    private String code;
}
