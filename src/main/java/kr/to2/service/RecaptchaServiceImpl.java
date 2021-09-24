package kr.to2.service;

import org.springframework.stereotype.Service;

import kr.to2.config.RecaptchaProperties;
import kr.to2.error.ApiErrorCode;
import kr.to2.error.ApiException;
import kr.to2.util.Strings;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RecaptchaServiceImpl implements RecaptchaService {

  private RecaptchaProperties recaptchaProperties;

  @Override
  public void verify(String recaptcha) {
    if (this.recaptchaProperties.isActive()) {
      if (Strings.isEmptyString(recaptcha)) {
        throw new ApiException(ApiErrorCode.RECAPTCHA_INVALID, "recaptcha 정보가 없습니다.");
      }

      // TODO: 실제 recaptcah 검증과 연결
    }
  }
  
}
