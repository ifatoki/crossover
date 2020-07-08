package com.company.resourceapi.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import io.swagger.models.Response;
import javassist.bytecode.ExceptionTable;

@ControllerAdvice
public class ProjectRestControllerAdvice {

  @ExceptionHandler({NotFoundSdlcSystemException.class, NotFoundException.class})
  public ResponseEntity<ErrorBody> notFoundExceptionHandler(RuntimeException ex) {
    ErrorBody error = new ErrorBody(
      HttpStatus.NOT_FOUND.value(),
      ex.getMessage(),
      ex.getClass().getSimpleName()
    );
    return new ResponseEntity<ErrorBody>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({InvalidRequestBodyException.class, HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
  public ResponseEntity<ErrorBody> invalidRequestBodyExceptionHandler(Exception ex) {
    String exceptionMessage;
    
    switch(ex.getClass().getSimpleName()) {
      case "MethodArgumentTypeMismatchException" :
        exceptionMessage = "invalid project Id";
        break;
      case "HttpMessageNotReadableException":
        exceptionMessage = "sdlcSystem in request body is invalid";
        break;
      default:
        exceptionMessage = ex.getMessage();
    }
    ErrorBody error = new ErrorBody(
      HttpStatus.BAD_REQUEST.value(),
      exceptionMessage,
      ex.getClass().getSimpleName()
    );
    return new ResponseEntity<ErrorBody>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ErrorBody> ConflictExceptionHandler(RuntimeException ex) {
    ErrorBody error = new ErrorBody(
      HttpStatus.CONFLICT.value(),
      ex.getMessage(),
      ex.getClass().getSimpleName()
    );
    return new ResponseEntity<ErrorBody>(error, HttpStatus.CONFLICT);
  }
}
