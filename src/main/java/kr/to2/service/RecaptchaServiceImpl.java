package kr.to2.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpResponse;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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

  private boolean vertifyFromGoogle(final String recaptcha) throws URISyntaxException, IOException, InterruptedException {
    final StringBuilder sb = new StringBuilder();
    sb.append("secret=");
    sb.append(this.recaptchaProperties.getPrivateKey());
    sb.append("&response=");
    sb.append(recaptcha);
    final BodyPublisher body = HttpRequest.BodyPublishers.ofString(sb.toString());
    
    final HttpRequest req = HttpRequest.newBuilder()
      .uri(new URI("https://www.google.com/recaptcha/api/siteverify"))
      .header("Content-Type", "application/x-www-form-urlencoded")
      .POST(body)
      .build();

    final HttpResponse<String> res = HttpClient.newBuilder().build()
      .send(req, HttpResponse.BodyHandlers.ofString());

    final ObjectMapper mapper = new ObjectMapper();
    final Map<String, Object> result = mapper.readValue(res.body(), new TypeReference<Map<String, Object>>(){});

    if (result.get("success").equals(Boolean.TRUE)) {
      return true;
    }

    return false;
  }

  @Override
  public void verify(String recaptcha) {
    if (this.recaptchaProperties.isActive()) {
      if (Strings.isEmpty(recaptcha)) {
        throw new ApiException(ApiErrorCode.RECAPTCHA_INVALID, "recaptcha 정보가 없습니다.");
      }

      boolean result = false;

      try {
        result = this.vertifyFromGoogle(recaptcha);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }

      if (!result) {
        throw new ApiException(ApiErrorCode.RECAPTCHA_INVALID, "recaptcha 검증을 통과하지 못했습니다.");
      }
    }
  }

}
