package kitchenpos.exception;

import org.springframework.http.HttpStatus;

public class CannotChangeOrderTableGuest extends KitchenposException {

    public CannotChangeOrderTableGuest() {
        super(HttpStatus.BAD_REQUEST, "테이블의 손님 수를 변경할 수 없습니다.");
    }
}
