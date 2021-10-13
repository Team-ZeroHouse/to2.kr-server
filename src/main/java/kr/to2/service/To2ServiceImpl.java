package kr.to2.service;

import java.util.Optional;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.to2.error.ApiErrorCode;
import kr.to2.error.ApiException;
import kr.to2.model.UrlCode;
import kr.to2.repository.UrlCodeRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Primary
public class To2ServiceImpl implements To2Service {

  private final UrlCodeRepository urlCodeRepository;

  private UrlValidator urlValidator = new UrlValidator(new String[] { "http", "https" });

  @Transactional
  @Override
  public String shorten(String url) {
    if (!this.urlValidator.isValid(url)) {
      throw new ApiException(ApiErrorCode.URL_INVALID, "올바르지 않은 url입니다.");
    }
    
    // 이미 DB에 존재하는 url이면 기존 데이터 반환
    final Optional<UrlCode> optionalUrlCode = this.urlCodeRepository.findByUrl(url);
    if (optionalUrlCode.isPresent()) {
      return optionalUrlCode.get().getCode();
    }

    final UrlCode urlCode = UrlCode.builder().url(url).build();
    this.urlCodeRepository.save(urlCode);
    final String code = this.convertLongToCode(urlCode.getId());
    urlCode.setCode(code);
    this.urlCodeRepository.save(urlCode);

    return code;
  }

  @Override
  public String findUrlByCode(String code) {
    final Optional<UrlCode> urlCode = this.urlCodeRepository.findByCode(code);
    return urlCode.orElseThrow(() -> {
      return new ApiException(ApiErrorCode.CODE_NOT_FOUND, "해당 code와 연관된 URL이 존재하지 않습니다.");
    }).getUrl();
  }

}
