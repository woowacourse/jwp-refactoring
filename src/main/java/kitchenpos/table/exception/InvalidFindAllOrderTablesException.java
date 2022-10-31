package kitchenpos.table.exception;

import kitchenpos.common.exception.InvalidRequestException;

public class InvalidFindAllOrderTablesException extends InvalidRequestException {

    public InvalidFindAllOrderTablesException() {
        super("아이디에 해당하는 주문 테이블을 찾을 수 없습니다.");
    }
}
