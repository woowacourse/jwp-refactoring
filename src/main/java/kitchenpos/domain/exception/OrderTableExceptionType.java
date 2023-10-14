package kitchenpos.domain.exception;

import kitchenpos.basic.BasicExceptionType;

public enum OrderTableExceptionType implements BasicExceptionType {

    NUMBER_OF_GUEST_LOWER_THAN_ZERO("손님의 수가 0보다 작을 수 없습니다.");

    private final String message;

    OrderTableExceptionType(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
