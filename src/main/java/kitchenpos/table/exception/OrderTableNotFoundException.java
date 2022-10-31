package kitchenpos.table.exception;

import kitchenpos.common.exception.InvalidRequestException;

public class OrderTableNotFoundException extends InvalidRequestException {

    public OrderTableNotFoundException() {
        super("주문 테이블을 찾을 수 없습니다.");
    }
}
