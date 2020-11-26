package kitchenpos.ui;

import kitchenpos.dto.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> processValidationError(MethodArgumentNotValidException exception) {
        String errorMessage = Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage();
        return ResponseEntity.
            badRequest().
            body(new ExceptionResponse(errorMessage));
    }
}
