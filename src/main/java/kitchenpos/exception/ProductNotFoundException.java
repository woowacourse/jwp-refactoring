package kitchenpos.exception;

public class ProductNotFoundException extends RuntimeException {
    private final static String error = "상품을 찾을 수 없습니다.";
    public ProductNotFoundException() {
        super(error);
    }
}
