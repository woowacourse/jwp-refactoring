package kitchenpos.exception;

public enum CustomErrorCode {

    PRICE_MIN_VALUE_ERROR("가격은 0원 이상이어야 합니다.");

    private final String message;

    CustomErrorCode(final String message) {
        this.message = message;
    }
}
