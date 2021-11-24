package kitchenpos.exception;

import org.springframework.http.HttpStatus;

public class InvalidMenuException extends KitchenposException {

    public InvalidMenuException() {
        super(HttpStatus.BAD_REQUEST, "유효하지 않은 메뉴입니다.");
    }
}
