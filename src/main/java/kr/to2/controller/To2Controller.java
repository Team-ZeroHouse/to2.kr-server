package kr.to2.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import kr.to2.dto.to2.ShortenRequest;
import kr.to2.dto.to2.ShortenResponse;
import kr.to2.service.RecaptchaService;
import kr.to2.service.To2Service;
import lombok.AllArgsConstructor;


@AllArgsConstructor
@Controller("/")
public class To2Controller {

  private To2Service to2Service;
  private RecaptchaService recaptchaService;
  
  @GetMapping()
  public String index() {
    return "index";
  }

  @ResponseBody
  @PostMapping("/shorten")
  public ShortenResponse shorten(@Valid @RequestBody ShortenRequest shortenRequest) {
    this.recaptchaService.verify(shortenRequest.getRecaptcha());
    final String shorenUrl = this.to2Service.shorten(shortenRequest.getUrl());
    return ShortenResponse.builder().shortenUrl(shorenUrl).build();
  }

  @ResponseBody
  @GetMapping("/{code:" + To2Service.CODE_REGEX + "}")
  public RedirectView code(@PathVariable String code) {
    final String url = this.to2Service.findUrlFromCode(code);

    final RedirectView redirectView = new RedirectView(url);
    redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
    return redirectView;
  }
  
}
