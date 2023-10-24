package kitchenpos.table.domain.exception;

import kitchenpos.basic.BasicExceptionType;

public enum TableGroupExceptionType implements BasicExceptionType {

    ORDER_TABLE_SIZE_IS_LOWER_THAN_ZERO_OR_EMPTY("주문 테이블의 사이즈가 2보다 작거나 비어있을 수 없습니다."),
    ORDER_TABLE_IS_NOT_EMPTY_OR_ALREADY_GROUPED("테이블이 비어있지 않거나, 다른 그룹에 이미 속해있습니다."),
    TABLE_GROUP_NOT_FOUND("요청하는 테이블 그룹을 찾을 수 없습니다."),
    ORDER_TABLE_IS_NOT_PRESENT_ALL("요청한 테이블들중 저장되지 않은 테이블이 있습니다.");

    private final String message;

    TableGroupExceptionType(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
