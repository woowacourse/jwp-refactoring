package kitchenpos.ui;

import kitchenpos.dto.common.ErrorResponse;
import kitchenpos.exception.common.BadRequestException;
import kitchenpos.exception.common.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, BadRequestException.class})
    ResponseEntity<ErrorResponse> handleIllegalArgumentException(Exception e) {
        return ResponseEntity.badRequest().body(ErrorResponse.of(e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<ErrorResponse> handleIllegalStateException(NotFoundException e) {
        return ResponseEntity.status(e.getErrorCode()).body(ErrorResponse.of(e.getMessage()));
    }
}
