package kitchenpos.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(final IllegalArgumentException e) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.from(e));
    }
}
