package kitchenpos.ui;

import kitchenpos.exception.KitchenPosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.internalServerError()
            .body("서버에 문제가 발생했습니다.");
    }

    @ExceptionHandler(KitchenPosException.class)
    public ResponseEntity<String> handle(KitchenPosException e) {
        return ResponseEntity.badRequest()
            .body(e.getMessage());
    }
}
