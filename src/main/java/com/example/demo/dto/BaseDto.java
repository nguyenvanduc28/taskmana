package com.example.demo.dto;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public abstract class BaseDto {
    public int id;
    public BigDecimal createdAt;
    public BigDecimal updatedAt;
}