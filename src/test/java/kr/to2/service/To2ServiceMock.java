package kr.to2.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import kr.to2.error.ApiErrorCode;
import kr.to2.error.ApiException;
import lombok.Getter;

@Getter
@Component
public class To2ServiceMock implements To2Service {

  private Map<String, String> urlToCode = new HashMap<>();
  private Map<String, String> codeToUrl = new HashMap<>();

  @Override
  public String shorten(String url) {
    if (this.urlToCode.containsKey(url)) {
      return this.urlToCode.get(url);
    }

    final long nextId = this.urlToCode.size() + 1;
    final String code = this.convertLongToCode(nextId);

    this.urlToCode.put(url, code);
    this.codeToUrl.put(code, url);

    return code;
  }

  @Override
  public String findUrlByCode(String code) {
    if (!this.codeToUrl.containsKey(code)) {
      throw new ApiException(ApiErrorCode.CODE_NOT_FOUND, "코드가 없습니다.");
    }

    return this.codeToUrl.get(code);
  }
  
}
