package kitchenpos.exception;

public enum OrderExceptionType implements BaseExceptionType {

    EMPTY_ORDER_LINE_ITEM_EXCEPTION("주문 항목이 존재하지 않습니다."),
    ORDER_TABLE_EMPTY_EXCEPTION("주문 테이블은 빈 상태이면 안됩니다."),
    NOT_EXIST_ORDER_EXCEPTION("주문이 존재하지 않습니다."),
    ORDER_STATUS_ALREADY_COMPLETION_EXCEPTION("주문 상태가 이미 완료된 상태입니다."),
    ORDER_STATUS_IS_NOT_COMPLETION_EXCEPTION("주문 상태가 완료된 상태가 아닙니다."),
    ;

    private final String message;

    OrderExceptionType(String message) {
        this.message = message;
    }

    @Override
    public String message() {
        return message;
    }
}
