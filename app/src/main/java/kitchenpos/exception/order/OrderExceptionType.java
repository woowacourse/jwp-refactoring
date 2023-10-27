package kitchenpos.exception.order;

import kitchenpos.exception.BaseExceptionType;

public enum OrderExceptionType implements BaseExceptionType {

    ORDER_NOT_FOUND("주문을 찾을 수 없습니다"),
    ORDER_TABLE_CAN_NOT_EMPTY("주문 테이블은 비어있을 수 없습니다"),
    ORDER_LINE_ITEMS_CAN_NOT_EMPTY("주문 항목은 비어있을 수 없습니다"),
    ORDER_LINE_ITEM_COMMANDS_CAN_NOT_NULL("주문 항목은 널일 수 없습니다"),
    CAN_NOT_CHANGE_COMPLETION_ORDER_STATUS("완료된 주문은 변경할 수 없습니다"),
    ORDER_STATUS_IS_COOKING_OR_MEAL("주문상태가 조리중이거나 식사중입니다"),
    ;

    private final String errorMessage;

    OrderExceptionType(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String errorMessage() {
        return errorMessage;
    }
}
