package kitchenpos.ui;

import kitchenpos.exception.BadRequestException;
import kitchenpos.exception.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionRestControllerAdvice {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage catchBadRequestExceptions(BadRequestException e) {
        return new ExceptionMessage(e.getMessage());
    }
}
