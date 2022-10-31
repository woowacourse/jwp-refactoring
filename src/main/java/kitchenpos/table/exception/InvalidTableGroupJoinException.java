package kitchenpos.table.exception;

import kitchenpos.common.exception.InvalidRequestException;

public class InvalidTableGroupJoinException extends InvalidRequestException {

    public InvalidTableGroupJoinException() {
        super("단체 지정을 할 수 없는 주문 테이블입니다.");
    }
}
