package kitchenpos.exception;

public class InvalidMenuPriceException extends IllegalArgumentException {

    private static final String MESSAGE = "메뉴의 가격이 상품 가격들의 합 보다 큽니다.";

    public InvalidMenuPriceException() {
        super(MESSAGE);
    }
}
