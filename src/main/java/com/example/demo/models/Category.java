package com.example.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
public class Category extends BaseEntity{
    @NotNull(message = "name is not null")
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
//    @OneToMany(mappedBy = "task")
//    private List<Task> tasks;
}