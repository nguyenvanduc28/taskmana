package com.example.demo.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface ITaskDto {
    int getId();
    BigDecimal getCreatedAt();
    BigDecimal getUpdatedAt();
    BigDecimal getCompletedAt();
    String getTitle();
    boolean getStatus();
    Integer getCategoryId();
}
