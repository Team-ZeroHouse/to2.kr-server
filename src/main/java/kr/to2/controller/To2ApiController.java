package kr.to2.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.to2.dto.to2.ShortenRequest;
import kr.to2.dto.to2.ShortenResponse;
import kr.to2.service.RecaptchaService;
import kr.to2.service.To2Service;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api")
public class To2ApiController {

  private RecaptchaService recaptchaService;
  private To2Service to2Service;
  
  @PostMapping("/shorten")
  public ShortenResponse shorten(@Valid @RequestBody ShortenRequest shortenRequest) {
    this.recaptchaService.verify(shortenRequest.getRecaptcha());
    
    final String shorenUrl = this.to2Service.shorten(shortenRequest.getUrl());

    return ShortenResponse.builder().shortenUrl(shorenUrl).build();
  }

}
