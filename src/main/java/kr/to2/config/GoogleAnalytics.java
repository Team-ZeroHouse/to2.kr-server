package kr.to2.config;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import kr.to2.util.Strings;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties("google-analytics")
public class GoogleAnalytics {

  private boolean active;

  private String measurementId;

  @PostConstruct
  private void postConstruct() {
    if (this.active) {
      if (Strings.isEmpty(this.measurementId)) {
        throw new RuntimeException("google-analytics.measurement-id가 필요합니다.");
      }
    }
  }

}
