package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto extends BaseDto{
    @NotEmpty(message = "is not empty")
    @NotBlank(message = "is not blank")
    private String name;
}
