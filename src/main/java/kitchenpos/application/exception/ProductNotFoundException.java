package kitchenpos.application.exception;

public class ProductNotFoundException extends IllegalArgumentException {

    public ProductNotFoundException() {
        super("지정한 상품을 찾을 수 없습니다.");
    }
}
