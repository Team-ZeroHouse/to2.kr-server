package kr.to2.dto.to2;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ShortenRequest {

  @NotBlank
  private String url;

  private String recaptcha;
  
}
