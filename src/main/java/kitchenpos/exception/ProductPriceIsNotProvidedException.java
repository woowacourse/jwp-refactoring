package kitchenpos.exception;

public class ProductPriceIsNotProvidedException extends RuntimeException {

    private static final String MESSAGE = "상품 가격이 제공되지 않았습니다.";

    public ProductPriceIsNotProvidedException() {
        super(MESSAGE);
    }
}
