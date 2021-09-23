package kr.to2.config;

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
}
