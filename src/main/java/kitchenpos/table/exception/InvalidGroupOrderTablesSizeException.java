package kitchenpos.table.exception;

import kitchenpos.common.exception.InvalidRequestException;

public class InvalidGroupOrderTablesSizeException extends InvalidRequestException {

    public InvalidGroupOrderTablesSizeException() {
        super("단체 지정에 필요한 최소 주문 테이블 크기가 아닙니다.");
    }
}
