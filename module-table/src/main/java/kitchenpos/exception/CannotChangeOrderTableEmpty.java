package kitchenpos.exception;

import org.springframework.http.HttpStatus;

public class CannotChangeOrderTableEmpty extends TableException {

    public CannotChangeOrderTableEmpty() {
        super(HttpStatus.BAD_REQUEST, "테이블의 빈 자리 여부를 바꿀 수 없습니다.");
    }
}
