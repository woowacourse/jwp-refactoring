package kitchenpos.exception;

import org.springframework.http.HttpStatus;

public class InvalidOrderTableException extends KitchenposException {

    public InvalidOrderTableException() {
        super(HttpStatus.BAD_REQUEST, "유효하지 않은 테이블 입니다.");
    }
}
