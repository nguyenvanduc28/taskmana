package com.example.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task")
public class Task extends BaseEntity{
    @NotBlank(message = "Title is not bank")
    private String title;
    @Column(name = "status", columnDefinition = "boolean default false")
    private boolean status;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @Column(name = "completed_at")
    private BigDecimal completedAt;
}
