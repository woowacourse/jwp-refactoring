package kitchenpos.menu.exception;

import kitchenpos.exception.BaseExceptionType;

public enum PriceExceptionType implements BaseExceptionType {

    PRICE_CAN_NOT_NULL("가격은 널일 수 없습니다"),
    PRICE_CAN_NOT_NEGATIVE("가격은 음수일 수 없습니다"),
    ;

    private final String errorMessage;

    PriceExceptionType(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String errorMessage() {
        return errorMessage;
    }
}
