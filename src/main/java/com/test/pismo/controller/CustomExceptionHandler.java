package com.test.pismo.controller;

import com.test.pismo.controller.response.ErrorResponse;
import com.test.pismo.exception.DuplicatedException;
import com.test.pismo.exception.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var errorList = ex.getBindingResult().getFieldErrors().stream().map(er -> ErrorResponse.builder().message(er.getDefaultMessage()).build()).toList();

        return handleExceptionInternal(ex, errorList, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {DuplicatedException.class})
    protected ResponseEntity<Object> handleDuplicatedException(RuntimeException ex, WebRequest request) {

        var error = ErrorResponse.builder().message(ex.getMessage()).build();

        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<Object> handleNotFoundException(RuntimeException ex, WebRequest request) {

        var error = ErrorResponse.builder().message(ex.getMessage()).build();

        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
