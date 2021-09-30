package kr.to2.dto.to2;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ShortenRequest {

  @Size(max = 500, message = "url의 최대 길이는 500입니다.")
  @NotBlank(message = "url은 비어있을 수 없습니다.")
  private String url;

  private String recaptcha;
  
}
