package com.example.demo.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AuthResponse {
    private String token;
    private UserInfoDto userInfoDto;
}
