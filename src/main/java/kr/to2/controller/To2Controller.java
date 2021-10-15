package kr.to2.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

import kr.to2.service.To2Service;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class To2Controller {

  @NonNull
  private To2Service to2Service;

  @Value("${spring.profiles.active:}")
  private String activeProfile;

  @GetMapping()
  public String index() {
    return "index";
  }

  @GetMapping("/{code:" + To2Service.CODE_REGEX + "}")
  public RedirectView code(@PathVariable String code) {
    log.info("code({}) 이동 요청", code);
    final String url = this.to2Service.findUrlByCode(code);
    log.info("code({}) -> url({}) 이동 성공", code, url);

    final RedirectView redirectView = new RedirectView(url);
    if (this.activeProfile.equals("prod")) {
      redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
    } else {
      redirectView.setStatusCode(HttpStatus.TEMPORARY_REDIRECT);
    }
    
    return redirectView;
  }
}
