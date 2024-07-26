package com.example.demo.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class AuthLoginDto {
    @NotEmpty(message = "is not empty")
    @NotBlank(message = "is not blank")
    private String email;
    @NotEmpty(message = "is not empty")
    @NotBlank(message = "is not blank")
    private String password;
}
