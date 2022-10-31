package kitchenpos.order.exception;

import kitchenpos.common.exception.InvalidRequestException;

public class InvalidOrderLineItemCreateException extends InvalidRequestException {

    public InvalidOrderLineItemCreateException() {
        super("주문 항목을 생성할 수 없습니다.");
    }
}
