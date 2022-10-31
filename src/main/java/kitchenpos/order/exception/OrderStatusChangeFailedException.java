package kitchenpos.order.exception;

import kitchenpos.common.exception.InvalidRequestException;

public class OrderStatusChangeFailedException extends InvalidRequestException {

    public OrderStatusChangeFailedException() {
        super("주문 상태 변경에 실패하였습니다.");
    }
}
