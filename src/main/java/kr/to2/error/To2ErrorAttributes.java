package kr.to2.error;

import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

@Component
public class To2ErrorAttributes extends DefaultErrorAttributes {
  
  @Override
	public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
		final Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);

    // 기본적으로 ApiException이 아니면 모두 500
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    final Throwable error = this.getError(webRequest);
    if (error instanceof ApiException) {
      final ApiException apiException = (ApiException) error;
      final ApiErrorCode apiErrorCode = apiException.getCode();
      errorAttributes.put("code", apiErrorCode);
      errorAttributes.put("message", apiException.getMessage());
      status = apiErrorCode.getStatus();
    } else {
      errorAttributes.put("message", "서버 내부에서 에러가 발생했습니다.");
    }

    errorAttributes.put("status", status.value());
    errorAttributes.put("error", status.getReasonPhrase());

    return errorAttributes;
	}

  // ResponseEntityExceptionHandler 참고 바람
  @Override
	public Throwable getError(WebRequest webRequest) {
		Throwable error = super.getError(webRequest);

    if (error instanceof HttpRequestMethodNotSupportedException) {
      error = new ApiException(ApiErrorCode.METHOD_NOT_ALLOWED, error.getMessage());
    } else if (error instanceof MethodArgumentNotValidException) {
      final MethodArgumentNotValidException ex = (MethodArgumentNotValidException) error;
      error = this.handleMethodArgumentNotValid(ex);
    } else if (error instanceof HttpMessageNotReadableException) {
      error = new ApiException(ApiErrorCode.BODY_NOT_VALID, "메세지 body가 올바르지 않습니다.");
    }

    return error;
	}

  protected ApiException handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
    StringBuilder sb = new StringBuilder();
    for (ObjectError error : ex.getAllErrors()) {
      sb.append("[");
      sb.append(error.getDefaultMessage());
      sb.append("] ");
    } 
    return new ApiException(ApiErrorCode.ARGUMENT_NOT_VALID, sb.toString());
  }
  
}
