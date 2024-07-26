package com.example.demo.exceptions;


import com.example.demo.exceptions.ProductManagerExceptions.ProductException;
import com.example.demo.exceptions.errorobject.ErrorObject;
import com.example.demo.exceptions.errorobject.MultiMessagesError;
import com.example.demo.exceptions.errorobject.SingleMessageError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorObject> handleHttpNotFoundException(NotFoundException ex, WebRequest webRequest) {
        ErrorObject errorObject = new SingleMessageError(HttpStatus.NOT_FOUND.value(), ex.getMessage(), new Date());
        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ErrorObject> handleUnauthorizedException(Exception ex, WebRequest webRequest) {
        ErrorObject errorObject = new SingleMessageError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), new Date());
        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorObject> handleHttpBadRequestException(BadRequestException ex, WebRequest webRequest) {
        ErrorObject errorObject = new SingleMessageError(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), new Date());
        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorObject> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest webRequest) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        if (ex.getBindingResult().getFieldErrors().size() == 1) {
            FieldError err = ex.getBindingResult().getFieldErrors().get(0);
            String message = err.getDefaultMessage();
            SingleMessageError errorObject = new SingleMessageError(HttpStatus.BAD_REQUEST.value(), message, new Date());
            return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.BAD_REQUEST);
        } else {
            List<String> messages = new ArrayList<>();
            for (String key : errors.keySet()) {
                messages.add(key + " " + errors.get(key));
            }
            MultiMessagesError errorObject = new MultiMessagesError(HttpStatus.BAD_REQUEST.value(), messages, new Date());
            return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorObject> handleConstraintViolationException(ConstraintViolationException ex, WebRequest webRequest) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(constraintViolation -> errors.put(constraintViolation.getPropertyPath().toString().split("\\.")[1], constraintViolation.getMessage()));
        if (ex.getConstraintViolations().size() == 1) {
            ConstraintViolation<?> err = ex.getConstraintViolations().iterator().next();
            String message = err.getPropertyPath().toString().split("\\.")[1] + " " + err.getMessage();
            SingleMessageError errorObject = new SingleMessageError(HttpStatus.BAD_REQUEST.value(), message, new Date());
            return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.BAD_REQUEST);
        } else {
            List<String> messages = new ArrayList<>();
            for (String key : errors.keySet()) {
                messages.add(errors.get(key));
            }
            MultiMessagesError errorObject = new MultiMessagesError(HttpStatus.BAD_REQUEST.value(), messages, new Date());
            return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorObject> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest webRequest) {
        String message = ex.getPropertyName() + " must be " + Objects.requireNonNull(ex.getRequiredType()).toString().split("\\.")[2].toLowerCase();
        SingleMessageError errorObject = new SingleMessageError(HttpStatus.BAD_REQUEST.value(), message, new Date());
        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorObject> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest webRequest) {
        SingleMessageError errorObject = new SingleMessageError(HttpStatus.BAD_REQUEST.value(), ex.getMessage().split(":")[0].toLowerCase(), new Date());
        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorObject> handleProductException(ProductException ex, WebRequest webRequest) {
        System.out.println(ex.getMessage());
        SingleMessageError errorObject = new SingleMessageError(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), new Date());
        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorObject> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        System.out.println(ex.getMessage());
        SingleMessageError errorObject = new SingleMessageError(HttpStatus.BAD_REQUEST.value(), "SKU đã tồn tại", new Date());
        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorObject> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex, WebRequest webRequest) {
        SingleMessageError errorObject = new SingleMessageError(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), new Date());
        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorObject> handleBadCredentialsException(BadCredentialsException ex) {
        SingleMessageError errorObject = new SingleMessageError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), new Date());
        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.UNAUTHORIZED);
    }
//
//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//    }
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorObject> handleAccessDeniedException(AccessDeniedException ex) {
        SingleMessageError errorObject = new SingleMessageError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), new Date());
        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.FORBIDDEN);
    }
}