package kr.to2.service;

import org.springframework.stereotype.Service;

@Service
public class To2ServiceImpl implements To2Service {

  @Override
  public String shorten(String url) {
    // TODO: 실제 Model 처리 로직으로 변경
    return "https://to2.kr/aaa";
  }

  @Override
  public String findUrlFromCode(String code) {
    // TODO: 실제 Model 처리 로직으로 변경
    return "https://github.com/Team-ZeroHouse/to2.kr-server";
  }
  
}
