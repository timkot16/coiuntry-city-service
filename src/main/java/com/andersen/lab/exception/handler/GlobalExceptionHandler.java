package com.andersen.lab.exception.handler;

import com.andersen.lab.exception.RegistrationException;
import com.andersen.lab.model.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegistrationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleRegistrationException(RegistrationException ex, WebRequest request) {
        log.error(ex.getClass().getSimpleName(), ex);
        return ErrorResponse.builder().message(ex.getMessage()).details(request.getDescription(false)).build();
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        log.error(ex.getClass().getSimpleName(), ex);
        return ErrorResponse.builder().message(ex.getMessage()).details(request.getDescription(false)).build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        log.error(ex.getClass().getSimpleName(), ex);
        return ErrorResponse.builder().message(ex.getMessage()).details(request.getDescription(false)).build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        log.error(ex.getClass().getSimpleName(), ex);
        return ErrorResponse.builder().message(ex.getMessage()).details(request.getDescription(false)).build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {
        log.error(ex.getClass().getSimpleName(), ex);
        return ErrorResponse.builder().message(ex.getMessage()).details(request.getDescription(false)).build();
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleIOException(IOException ex, WebRequest request) {
        log.error(ex.getClass().getSimpleName(), ex);
        return ErrorResponse.builder().message(ex.getMessage()).details(request.getDescription(false)).build();
    }

}
