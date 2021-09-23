package kr.to2.config;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties("recaptcha")
public class RecaptchaProperties {
  private String siteKey;
  private String privateKey;
  private boolean active;

  @PostConstruct
  private void postConstruct() {
    if (this.active) {
      if (isEmptyString(this.siteKey) || isEmptyString(this.privateKey)) {
        throw new RuntimeException("recaptcha.siteKey 또는 recaptcha.privateKey가 필요합니다.");
      }
    }
  }

  private static boolean isEmptyString(String string) {
    return string == null || string.isBlank();
  }
}
