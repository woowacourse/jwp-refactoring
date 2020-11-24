package kitchenpos.table.exception;

import kitchenpos.common.CreateFailException;

public class InvalidTableQuantityException extends CreateFailException {
    public InvalidTableQuantityException() {
        super("테이블 그룹을 만드는데, 잘못된 테이블 갯수를 입력했습니다.");
    }
}
