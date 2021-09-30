package kr.to2.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public enum ApiErrorCode {

  // TODO: 추후 DDD로 변경시 모두 분리 관리
  RECAPTCHA_INVALID(HttpStatus.BAD_REQUEST),
  CODE_NOT_FOUND(HttpStatus.NOT_FOUND),
  URL_INVALID(HttpStatus.BAD_REQUEST);

  @Getter
  private final HttpStatus status;

  private ApiErrorCode(HttpStatus status) {
    this.status = status;
  }
}
