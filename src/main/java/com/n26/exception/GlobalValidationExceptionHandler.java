package com.n26.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalValidationExceptionHandler {

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<?> handleJsonException(
      HttpServletResponse response, HttpMessageNotReadableException ex) {
    if (ex.getRootCause() instanceof InvalidFormatException) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }
    return ResponseEntity.badRequest().build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleJsonException(
      HttpServletResponse response, MethodArgumentNotValidException ex) {
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
  }

  @ExceptionHandler(NumberFormatException.class)
  public ResponseEntity<?> handleJsonException(
      HttpServletResponse response, NumberFormatException ex) {
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
  }
}
