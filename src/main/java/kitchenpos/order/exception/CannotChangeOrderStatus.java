package kitchenpos.order.exception;

import org.springframework.http.HttpStatus;

public class CannotChangeOrderStatus extends OrderException {

    public CannotChangeOrderStatus() {
        super(HttpStatus.BAD_REQUEST, "주문 상태를 바꿀 수 없습니다.");
    }
}
