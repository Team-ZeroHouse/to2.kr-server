package kr.to2.error;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class To2ErrorController extends BasicErrorController {

  private final ErrorAttributes errorAttributes;

  public To2ErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties,
      ObjectProvider<ErrorViewResolver> errorViewResolvers) {
    super(errorAttributes, serverProperties.getError(),
        errorViewResolvers.orderedStream().collect(Collectors.toList()));
    this.errorAttributes = errorAttributes;
  }

  @Override
  protected HttpStatus getStatus(HttpServletRequest request) {
    final WebRequest webRequest = new ServletWebRequest(request);
    final Throwable error = this.errorAttributes.getError(webRequest);

    if (error == null) {
      return super.getStatus(request);
    }

    if (error instanceof ApiException) {
      final ApiException apiException = (ApiException) error;
      return apiException.getCode().getStatus();
    }

    // 그외 필요한 에러들 핸들링, 그렇지 않으면 모두 500
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }

}
