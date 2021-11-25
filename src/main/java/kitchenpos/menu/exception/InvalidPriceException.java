package kitchenpos.menu.exception;

import kitchenpos.exception.KitchenposException;
import org.springframework.http.HttpStatus;

public class InvalidPriceException extends KitchenposException {

    public InvalidPriceException() {
        super(HttpStatus.BAD_REQUEST, "유효하지 않은 Price 입니다.");
    }
}
