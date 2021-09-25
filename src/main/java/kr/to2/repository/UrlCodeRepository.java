package kr.to2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.to2.model.UrlCode;

@Repository
public interface UrlCodeRepository extends JpaRepository<UrlCode, Long> {

  Optional<UrlCode> findByCode(String code);
  
  Optional<UrlCode> findByUrl(String url);

}
