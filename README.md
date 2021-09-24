# to2.kr

주소를 짧게 줄여주는 서비스

## External Application Configuration

소스코드 내부에 민감정보를 포함시키지 않기 위해서 [features.external-config.files.wildcard-locations](https://docs.spring.io/spring-boot/docs/2.5.x/reference/html/features.html#features.external-config.files.wildcard-locations) 기능을 사용하여 설정을 관리합니다.

정상적인 개발과 배포를 위해서 개발시에는 `project-root/config`에 그리고 배포시에는 `jar-directory/config`에 아래의 파일을 생성하기 바랍니다.

- config/recaptcha/application.yml

`config/<config-name>/application.yml`의 `<config-name>`에 해당하는 설정이 `src/main/resources/application.yml`에 샘플로 빈값 또는 기본값이 설정돼 있습니다. 이에 맞게 파일을 작성하면 됩니다.
