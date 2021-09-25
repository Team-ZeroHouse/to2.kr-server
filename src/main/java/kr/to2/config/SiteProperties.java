package kr.to2.config;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties("site")
public class SiteProperties {
  
  @NotBlank
  private String protocolAndHost;

}
