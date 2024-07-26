package com.example.demo.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePasswordDto {
    @NotEmpty(message = "is not empty")
    @NotBlank(message = "is not blank")
    private String currentPassword;
    @NotEmpty(message = "is not empty")
    @NotBlank(message = "is not blank")
    private String newPassword;
    @NotEmpty(message = "is not empty")
    @NotBlank(message = "is not blank")
    private String confirmPassword;
}
