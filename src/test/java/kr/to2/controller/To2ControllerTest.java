package kr.to2.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import kr.to2.error.ApiErrorCode;
import kr.to2.error.ApiException;
import kr.to2.service.To2ServiceMock;

@WebMvcTest(To2Controller.class)
@ActiveProfiles("test")
public class To2ControllerTest {

  @Autowired
  private MockMvc mvc;

  @SpyBean
  private To2ServiceMock to2Service;

  @BeforeEach
  public void beforeEach() {
    this.to2Service.getCodeToUrl().clear();
    this.to2Service.getUrlToCode().clear();
  }

  @Test
  public void GET_존재하는_코드_test() throws Exception {
    final String url = "https://to2.kr";
    final String code = this.to2Service.shorten(url);

    this.mvc.perform(get("/{code}", code)).andExpect(redirectedUrl(url));
  }

  @Test
  public void GET_존재하지_않는_코드_test() throws Exception {
    final String code = "aaa";

    this.mvc.perform(get("/{code}", code))
      .andExpect(result -> assertTrue(result.getResolvedException() instanceof ApiException))
      .andExpect(result -> assertEquals(ApiErrorCode.CODE_NOT_FOUND, ((ApiException)result.getResolvedException()).getCode()));
  }

}
