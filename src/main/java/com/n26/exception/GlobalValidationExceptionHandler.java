package com.n26.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalValidationExceptionHandler {

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Object> handleJsonException(
      HttpServletResponse response, HttpMessageNotReadableException ex) {
    if (ex.getRootCause() instanceof InvalidFormatException) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }
    return ResponseEntity.badRequest().build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleJsonException(
      HttpServletResponse response, MethodArgumentNotValidException ex) {
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
  }

  @ExceptionHandler(NumberFormatException.class)
  public ResponseEntity<Object> handleJsonException(
      HttpServletResponse response, NumberFormatException ex) {
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
  }
}
