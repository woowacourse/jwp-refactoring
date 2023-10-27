package kitchenpos.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<String> handleException(BaseException e) {
        BaseExceptionType exceptionType = e.exceptionType();
        return ResponseEntity.badRequest().body(exceptionType.errorMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.internalServerError().body("알 수 없는 오류가 발생했습니다");
    }
}
