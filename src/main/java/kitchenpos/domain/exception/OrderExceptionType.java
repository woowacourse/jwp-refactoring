package kitchenpos.domain.exception;

import kitchenpos.basic.BasicExceptionType;

public enum OrderExceptionType implements BasicExceptionType {

    ORDER_IS_ALREADY_COMPLETION("이미 완료 상태의 주문입니다."),
    ORDER_IS_NOT_COMPLETION("완료되지 않은 상태의 주문입니다.");

    private final String message;

    OrderExceptionType(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
