package kitchenpos.table.exception;

import kitchenpos.common.CreateFailException;

public class IllegalTableStateException extends CreateFailException {
    public IllegalTableStateException() {
        super("테이블상태가 그룹을 만들 수 없습니다.");
    }
}
