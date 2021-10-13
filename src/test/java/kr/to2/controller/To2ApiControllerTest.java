package kr.to2.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import kr.to2.config.SiteProperties;
import kr.to2.dto.to2.ShortenRequest;
import kr.to2.service.RecaptchaService;
import kr.to2.service.To2ServiceMock;

@WebMvcTest(To2ApiController.class)
@ActiveProfiles("test")
public class To2ApiControllerTest {

  @Autowired
  private MockMvc mvc;

  @SpyBean
  private To2ServiceMock to2Service;

  @SpyBean
  private SiteProperties siteProperties;

  @MockBean
  private RecaptchaService recaptchaService;

  @Autowired
  private ObjectMapper mapper;

  @Test
  public void POST_api_shorten_test() throws Exception {
    final String url = "https://to2.kr";

    final ShortenRequest reqBody = new ShortenRequest();
    reqBody.setUrl(url);
    final String json = this.mapper.writeValueAsString(reqBody);

    final var req = post("/api/shorten")
      .contentType(MediaType.APPLICATION_JSON)
      .content(json)
      .accept(MediaType.APPLICATION_JSON);
    
    this.mvc.perform(req)
      .andExpect(status().isOk())
      .andExpect(
        jsonPath("$.shortenUrl")
          .value(this.siteProperties.getProtocolAndHost() + "/" + this.to2Service.getUrlToCode().get(url))
      );
  }

}
