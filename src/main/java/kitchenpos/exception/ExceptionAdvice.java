package kitchenpos.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(KitchenposException.class)
    public ResponseEntity<ExceptionResponse> KitchenposExceptionHandler(KitchenposException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> exceptionHandler(Exception e) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ExceptionResponse(e.getMessage()));
    }
}
