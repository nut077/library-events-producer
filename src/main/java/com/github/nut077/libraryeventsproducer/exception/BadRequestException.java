package com.github.nut077.libraryeventsproducer.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends CommonException {

  public BadRequestException(String message) {
    super(HttpStatus.BAD_REQUEST, String.valueOf(HttpStatus.BAD_REQUEST.value()), message);
  }
}
