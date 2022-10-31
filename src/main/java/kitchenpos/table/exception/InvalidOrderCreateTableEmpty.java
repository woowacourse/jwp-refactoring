package kitchenpos.table.exception;

import kitchenpos.common.exception.InvalidRequestException;

public class InvalidOrderCreateTableEmpty extends InvalidRequestException {

    public InvalidOrderCreateTableEmpty() {
        super("주문 테이블이 비어있어 주문 생성을 할 수 없습니다.");
    }
}
