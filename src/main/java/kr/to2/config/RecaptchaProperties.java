package kr.to2.config;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import kr.to2.util.Strings;
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
      if (Strings.isEmpty(this.siteKey)) {
        throw new RuntimeException("recaptcha.siteKey가 필요합니다.");
      }
      if (Strings.isEmpty(this.privateKey)) {
        throw new RuntimeException("recaptcha.privateKey가 필요합니다.");
      }
    }
  }

}
