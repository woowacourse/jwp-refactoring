package kitchenpos.order.exception;

import kitchenpos.common.exception.InvalidRequestException;

public class InvalidChangedEmptyRequest extends InvalidRequestException {

    public InvalidChangedEmptyRequest() {
        super("주문 테이블 상태를 바꿀 수 없습니다.");
    }
}
