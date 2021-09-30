package kr.to2.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public enum ApiErrorCode {

  RECAPTCHA_INVALID(HttpStatus.BAD_REQUEST),
  CODE_NOT_FOUND(HttpStatus.NOT_FOUND),
  URL_INVALID(HttpStatus.BAD_REQUEST),
  METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED),
  ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST),
  BODY_NOT_VALID(HttpStatus.BAD_REQUEST);

  @Getter
  private final HttpStatus status;

  private ApiErrorCode(HttpStatus status) {
    this.status = status;
  }
}
