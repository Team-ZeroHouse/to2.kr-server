package kr.to2.error;

import lombok.Getter;

public class ApiException extends RuntimeException {

  @Getter
  private ApiErrorCode code;

  public ApiException(ApiErrorCode code, String message) {
    super(message);
    this.code = code;
  }
  
}
