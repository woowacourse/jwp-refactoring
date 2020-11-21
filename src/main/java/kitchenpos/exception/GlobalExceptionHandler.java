package kitchenpos.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleBadRequest(IllegalArgumentException e) {
        logger.info(e.getMessage());

        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleBadRequest(Exception e) {
        logger.error(e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
