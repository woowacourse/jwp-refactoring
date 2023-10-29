package kitchenpos.table.domain.exception;

import kitchenpos.basic.BasicExceptionType;

public enum OrderTableExceptionType implements BasicExceptionType {

    NUMBER_OF_GUEST_LOWER_THAN_ZERO("손님의 수가 0보다 작을 수 없습니다."),
    ORDER_TABLE_IS_NOT_FOUND("테이블을 찾을 수 없습니다."),
    TABLE_CANT_CHANGE_EMPTY_ALREADY_IN_GROUP("테이블 그룹에 속해있어서 상태를 변경할 수 없습니다."),
    ORDER_IS_NOT_COMPLETION("완료되지 않은 상태의 주문입니다."),
    TABLE_CANT_CHANGE_NUMBER_OF_GUESTS_EMPTY("테이블이 비어있는 경우 게스트 수를 변경할 수 없습니다.");

    private final String message;

    OrderTableExceptionType(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
