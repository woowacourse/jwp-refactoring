package kitchenpos.menugroup.exception;

import kitchenpos.exception.KitchenposException;
import org.springframework.http.HttpStatus;

public class InvalidMenuGroupException extends KitchenposException {

    public InvalidMenuGroupException() {
        super(HttpStatus.BAD_REQUEST, "유효하지 않은 MenuGroup 입니다.");
    }
}
