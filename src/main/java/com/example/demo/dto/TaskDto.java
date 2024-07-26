package com.example.demo.dto;

import com.example.demo.models.Category;
import com.example.demo.models.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class TaskDto extends BaseDto{

    @NotBlank(message = "Title is not bank")
    private String title;
    private boolean status;
    private CategoryDto category;
    private BigDecimal completedAt;
}
