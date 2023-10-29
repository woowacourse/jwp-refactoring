package kitchenpos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(IllegalArgumentException.class)
    public void handleBadRequestException(final IllegalArgumentException e) {
        log.warn("exception from handleBadRequestException = ", e);
    }

    @ExceptionHandler(Exception.class)
    public void handleUnExpectedException(final Exception e) {
        log.error("exception from handleUnExpectedException = ", e);
    }
}
