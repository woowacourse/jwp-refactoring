package kitchenpos.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kitchenpos.common.dto.ErrorResponse;
import lombok.extern.log4j.Log4j2;

@RestControllerAdvice
@Log4j2
public class ExceptionControllerAdvice {

    @ExceptionHandler(CreateFailException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse createFailExceptionHandle(Exception e) {
        log.info(e.getMessage());
        return new ErrorResponse("잘못된 인자 입력입니다.");
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse runtimeExceptionHandle(Exception e) {
        log.info(e.getMessage());
        return new ErrorResponse("잘못된 요청입니다.");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse exceptionHandle(Exception e) {
        log.warn(e.getMessage());
        return new ErrorResponse("서버의 문제가 발생했습니다.");
    }
}
