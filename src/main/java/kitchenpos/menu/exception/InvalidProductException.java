package kitchenpos.menu.exception;

import org.springframework.http.HttpStatus;

public class InvalidProductException extends MenuException {

    public InvalidProductException() {
        super(HttpStatus.BAD_REQUEST, "유효하지 않은 Product 입니다.");
    }
}
