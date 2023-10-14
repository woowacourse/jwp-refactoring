package kitchenpos.exception;

public class ProductPriceIsNegativeException extends RuntimeException {

    private static final String MESSAGE = "상품 가격이 0원 미만입니다.";

    public ProductPriceIsNegativeException() {
        super(MESSAGE);
    }
}
