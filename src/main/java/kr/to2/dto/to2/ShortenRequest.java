package kr.to2.dto.to2;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ShortenRequest {

  @Size(max = 500)
  @NotBlank
  private String url;

  private String recaptcha;
  
}
