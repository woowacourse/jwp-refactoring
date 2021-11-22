package kitchenpos.exception;

public class InvalidPriceException extends KitchenPosException {

    private static final String INVALID_PRICE = "유효하지 않은 가격입니다.";

    public InvalidPriceException() {
        super(INVALID_PRICE);
    }
}
