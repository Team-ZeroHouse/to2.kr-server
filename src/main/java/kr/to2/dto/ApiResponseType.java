package kr.to2.dto;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/*
오류  |  Cloud API  |  Google Cloud (오류 처리)
https://cloud.google.com/apis/design/errors#handling_errors
를 참고해서 작성
*/

public enum ApiResponseType {

  OK(HttpStatus.OK),
  INVALID_ARGUMENT(HttpStatus.BAD_REQUEST),
  NOT_FOUND(HttpStatus.NOT_FOUND);

  @Getter
  private final HttpStatus status;
  
  private ApiResponseType(HttpStatus status) {
    this.status = status;
  }

}
