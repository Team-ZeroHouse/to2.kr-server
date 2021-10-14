package kr.to2.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.to2.config.SiteProperties;
import kr.to2.dto.to2.ShortenRequest;
import kr.to2.dto.to2.ShortenResponse;
import kr.to2.service.RecaptchaService;
import kr.to2.service.To2Service;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "/api")
public class To2ApiController {

  private RecaptchaService recaptchaService;
  private To2Service to2Service;
  private SiteProperties siteProperties;
  
  @PostMapping("/shorten")
  public ShortenResponse shorten(@Valid @RequestBody ShortenRequest shortenRequest) {
    this.recaptchaService.verify(shortenRequest.getRecaptcha());
    
    final String url = shortenRequest.getUrl();
    log.info("url({}) 줄이기 요청", url);
    final String code = this.to2Service.shorten(url);
    log.info("url({}) -> code({}) 줄이기 성공", url, code);
    final String shortenUrl = this.siteProperties.getProtocolAndHost() + "/" + code;

    return ShortenResponse.builder().shortenUrl(shortenUrl).build();
  }

}
