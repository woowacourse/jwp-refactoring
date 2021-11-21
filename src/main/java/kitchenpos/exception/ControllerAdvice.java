package kitchenpos.exception;

import kitchenpos.exception.dto.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(KitchenPosException.class)
    public ResponseEntity<ExceptionResponse> handleKitchenPosException(KitchenPosException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getBody());
    }
}
