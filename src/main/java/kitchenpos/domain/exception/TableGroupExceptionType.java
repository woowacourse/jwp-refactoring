package kitchenpos.domain.exception;

import kitchenpos.basic.BasicExceptionType;

public enum TableGroupExceptionType implements BasicExceptionType {

    ORDER_TABLE_SIZE_IS_LOWER_THAN_ZERO_OR_EMPTY("주문 테이블의 사이즈가 2보다 작거나 비어있을 수 없습니다."),
    ORDER_TABLE_IS_NOT_EMPTY_OR_ALREADY_GROUPED("테이블이 비어있지 않거나, 다른 그룹에 이미 속해있습니다.");

    private final String message;

    TableGroupExceptionType(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
