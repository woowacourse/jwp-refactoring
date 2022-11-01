package kitchenpos.exception;

public class ProductNegativePriceException extends RuntimeException {
    public ProductNegativePriceException() {
        super("제품의 가격이 Null이거나 음수 일 수 없습니다.");
    }
}
