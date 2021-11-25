package kitchenpos.order.exception;

import kitchenpos.exception.KitchenposException;
import org.springframework.http.HttpStatus;

public class CannotChangeOrderStatus extends KitchenposException {

    public CannotChangeOrderStatus() {
        super(HttpStatus.BAD_REQUEST, "주문 상태를 바꿀 수 없습니다.");
    }
}
