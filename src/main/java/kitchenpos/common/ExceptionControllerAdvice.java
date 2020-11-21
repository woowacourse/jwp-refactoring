package kitchenpos.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.log4j.Log4j2;

@RestControllerAdvice
@Log4j2
public class ExceptionControllerAdvice {

    @ExceptionHandler(CreateFailException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public void createFailExceptionHandle(Exception e) {
        log.info("잘못된 요청입니다. {}", e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public void runtimeExceptionHandle(Exception e) {
        log.info("잘못된 요청입니다. {}", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public void exceptionHandle(Exception e) {
        log.warn("서버 에러입니다. {}", e.getMessage());
    }
}
