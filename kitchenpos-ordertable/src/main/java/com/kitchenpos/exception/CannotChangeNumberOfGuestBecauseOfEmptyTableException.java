package com.kitchenpos.exception;

public class CannotChangeNumberOfGuestBecauseOfEmptyTableException extends RuntimeException {

    public CannotChangeNumberOfGuestBecauseOfEmptyTableException() {
        super("비어 있는 테이블에 손님 수를 변경할 수 없습니다.");
    }
}
