package kr.to2.dto.to2;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShortenResponse {
  
  private String shortenUrl;

}
