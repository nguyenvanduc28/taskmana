package com.example.demo.exceptions.errorobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class ErrorObject {
    private int errorCode;
    private int responseCode;
    private Date timestamp;

    public ErrorObject(int responseCode, Date timestamp) {
        this.errorCode = -1;
        this.responseCode = responseCode;
        this.timestamp = timestamp;
    }
}
