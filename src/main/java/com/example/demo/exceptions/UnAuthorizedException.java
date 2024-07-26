package com.example.demo.exceptions;

public class UnAuthorizedException extends RuntimeException {

    public UnAuthorizedException() {
        super("Unauthorized");
    }
}
