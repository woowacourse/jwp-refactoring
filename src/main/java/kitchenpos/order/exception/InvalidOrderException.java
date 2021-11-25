package kitchenpos.order.exception;

import kitchenpos.exception.KitchenposException;
import org.springframework.http.HttpStatus;

public class InvalidOrderException extends KitchenposException {

    public InvalidOrderException() {
        super(HttpStatus.BAD_REQUEST, "유효하지 않은 주문입니다.");
    }
}
