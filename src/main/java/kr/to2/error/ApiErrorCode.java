package kr.to2.error;

import kr.to2.dto.ApiResponseType;
import lombok.Getter;


public enum ApiErrorCode {

  // TODO: 추후 DDD로 변경시 모두 분리 관리
  RECAPTCHA_INVALID(ApiResponseType.INVALID_ARGUMENT);

  @Getter
  private final ApiResponseType type;

  private ApiErrorCode(ApiResponseType type) {
    this.type = type;
  }
}
