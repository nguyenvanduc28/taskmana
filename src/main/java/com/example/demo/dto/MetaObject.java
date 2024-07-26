package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MetaObject {
    private int page;
    private int perPage;
    private long totalItems;
    private int totalPages;
}
