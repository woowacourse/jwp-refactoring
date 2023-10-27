package kitchenpos.exception.table;

import kitchenpos.exception.BaseExceptionType;

public enum OrderTableExceptionType implements BaseExceptionType {

    ORDER_TABLE_NOT_FOUND("주문 테이블을 찾을 수 없습니다"),
    CAN_NOT_CHANGE_EMPTY_GROUPED_ORDER_TABLE("지정된 단체가 있습니다"),
    NUMBER_OF_GUESTS_CAN_NOT_NEGATIVE("손님 숫자는 음수일 수 없습니다"),
    CAN_NOT_CHANGE_NUMBER_OF_GUESTS_EMPTY_ORDER_TABLE("테이블이 비어 있습니다"),
    ;

    private final String errorMessage;

    OrderTableExceptionType(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String errorMessage() {
        return errorMessage;
    }
}
