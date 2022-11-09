package kitchenpos.menu.exception;

public class ProductNotFoundException extends IllegalArgumentException {

    private static final String MESSAGE = "존재하지 않는 상품입니다.";

    public ProductNotFoundException() {
        super(MESSAGE);
    }
}
