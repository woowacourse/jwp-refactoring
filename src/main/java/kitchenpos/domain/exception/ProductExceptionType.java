package kitchenpos.domain.exception;

import kitchenpos.basic.BasicExceptionType;

public enum ProductExceptionType implements BasicExceptionType {

    PRICE_IS_LOWER_THAN_ZERO("가격은 0보다 작을 수 없습니다."),
    PRICE_IS_NULL("가격은 0보다 작을 수 없습니다.");

    private final String message;

    ProductExceptionType(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
