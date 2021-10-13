package kr.to2.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ApiException extends RuntimeException {

  @Getter
  private ApiErrorCode code;

  public ApiException(ApiErrorCode code, String message) {
    super(message);
    this.code = code;
  }
  
}
