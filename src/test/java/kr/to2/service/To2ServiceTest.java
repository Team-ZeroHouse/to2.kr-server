package kr.to2.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import kr.to2.model.UrlCode;
import kr.to2.repository.UrlCodeRepository;

@SpringBootTest
@ActiveProfiles("test")
public class To2ServiceTest {
  
  @Autowired
  private UrlCodeRepository repository;

  @Autowired
  private To2Service service;

  @BeforeEach
  public void beforeEach() {
    this.repository.deleteAll();
  }

  @Test void convertLongToCode_test() {
    assertEquals("aaa", this.service.convertLongToCode(1));
    assertEquals("cWB", this.service.convertLongToCode(9702));
    assertEquals("dcZ", this.service.convertLongToCode(10611));
  }

  @Test
  public void shorten_test() {
    final String url = "https://to2.kr";
    final String code = this.service.shorten(url);

    final UrlCode foundUrlCode = this.repository.findByUrl(url).get();
    assertEquals(foundUrlCode.getCode(), code);
  }

  @Test
  public void findUrlByCode_test() {
    final String url = "https://to2.kr";
    final String code = "zzz";

    final UrlCode urlCode = UrlCode.builder().url(url).code(code).build();
    this.repository.save(urlCode);

    final String foundUrl = this.service.findUrlByCode(code);
    assertEquals(url, foundUrl);
  }

}
