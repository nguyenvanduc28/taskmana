package com.example.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class UserEntity extends BaseEntity {
    @NotBlank(message = "Email is not bank")
    private String email;
    @Column(name = "name")
    @NotBlank(message = "Name is not bank")
    private String name;
    @NotBlank(message = "Password is not bank")
    private String password;
    private BigDecimal dob;
//    @OneToMany(mappedBy = "taskEntity")
//    private List<Task> taskList;
}
