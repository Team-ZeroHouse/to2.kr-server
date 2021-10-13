package kr.to2.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import kr.to2.model.UrlCode;

@DataJpaTest
public class UrlCodeRepositoryTest {
  
  @Autowired
  private UrlCodeRepository repository;

  @BeforeEach
  public void beforeEach() {
    this.repository.deleteAll();
  }

  @Test
  public void findByUrl_test() {
    final String url = "https://to2.kr";
    final String code = "zzz";

    final UrlCode urlCode = UrlCode.builder().url(url).code(code).build();
    this.repository.save(urlCode);
    
    final Optional<UrlCode> urlCodeOp = this.repository.findByUrl(url);
    final UrlCode found = urlCodeOp.get();
    
    assertEquals(code, found.getCode());
  }

  @Test
  public void findByCode_test() {
    final String url = "https://to2.kr";
    final String code = "abc";

    final UrlCode urlCode = UrlCode.builder().url(url).code(code).build();
    this.repository.save(urlCode);
    
    final Optional<UrlCode> urlCodeOp = this.repository.findByCode(code);
    final UrlCode found = urlCodeOp.get();
    
    assertEquals(url, found.getUrl());
  }

}
