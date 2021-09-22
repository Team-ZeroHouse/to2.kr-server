package kr.to2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/")
public class TestController {

  @GetMapping()
  public String test() {
    return "home";
  }
}
