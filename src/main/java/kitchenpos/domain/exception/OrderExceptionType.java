package kitchenpos.domain.exception;

import kitchenpos.basic.BasicExceptionType;

public enum OrderExceptionType implements BasicExceptionType {

    ORDER_IS_ALREADY_COMPLETION("이미 완료 상태의 주문입니다."),
    ORDER_IS_NOT_COMPLETION("완료되지 않은 상태의 주문입니다."),
    ORDER_IS_NOT_FOUND("요청한 Order를 찾을 수 없습니다."),
    ORDER_LINE_ITEM_IS_NOT_PRESENT_ALL("요청한 상품들중 저장되지 않은 상품이 있습니다."),
    ORDER_TABLE_IS_EMPTY("주문을 요청한 테이블이 비어있습니다."),
    ORDER_LINE_ITEM_DTOS_EMPTY("주문에 상품이 존재하지 않습니다");

    private final String message;

    OrderExceptionType(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
