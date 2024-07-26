package com.example.demo.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PagedResponseObject {
    private int responseCode;
    private String message;
    private MetaObject meta;
    private Object data;
}
