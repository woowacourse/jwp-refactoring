package kitchenpos.table.exception;

import org.springframework.http.HttpStatus;

public class CannotUngroupTableException extends TableException {

    public CannotUngroupTableException() {
        super(HttpStatus.BAD_REQUEST, "해제할 수 없는 테이블 그룹 입니다.");
    }
}
