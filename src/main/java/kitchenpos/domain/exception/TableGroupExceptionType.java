package kitchenpos.domain.exception;

import kitchenpos.basic.BasicExceptionType;

public enum TableGroupExceptionType implements BasicExceptionType {

    ORDER_TABLE_SIZE_IS_LOWER_THAN_ZERO_OR_EMPTY("주문 테이블의 사이즈가 2보다 작거나 비어있을 수 없습니다.");

    private final String message;

    TableGroupExceptionType(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
