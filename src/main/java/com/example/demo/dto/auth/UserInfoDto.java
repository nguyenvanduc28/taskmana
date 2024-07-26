package com.example.demo.dto.auth;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class UserInfoDto {
    private String email;
    private String name;
    private BigDecimal dob;
}
