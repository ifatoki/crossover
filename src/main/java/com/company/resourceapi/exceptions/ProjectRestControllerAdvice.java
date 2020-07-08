package com.company.resourceapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ProjectRestControllerAdvice {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorBody> notFoundExceptionHandler(NotFoundException ex) {
    ErrorBody error = new ErrorBody(HttpStatus.NOT_FOUND.value(), ex.getMessage(), ex.getClass().getSimpleName());
    return new ResponseEntity<ErrorBody>(error, HttpStatus.NOT_FOUND);
  }
}
