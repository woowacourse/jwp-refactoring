package kitchenpos.ui;

import kitchenpos.exception.ExceptionMessage;
import kitchenpos.exception.InvalidRequestParamException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionRestControllerAdvice {

    @ExceptionHandler(InvalidRequestParamException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage invalidRequestParamException(InvalidRequestParamException e) {
        return new ExceptionMessage(e.getMessage());
    }
}
