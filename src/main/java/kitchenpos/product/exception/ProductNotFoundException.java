package kitchenpos.product.exception;

public class ProductNotFoundException extends RuntimeException {

    private static final String MESSAGE = "상품을 찾을 수 없습니다.";

    public ProductNotFoundException() {
        super(MESSAGE);
    }
}
