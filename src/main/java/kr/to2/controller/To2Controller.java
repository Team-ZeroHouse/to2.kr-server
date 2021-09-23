package kr.to2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/")
public class To2Controller {

  @GetMapping()
  public String index() {
    return "index";
  }
}
