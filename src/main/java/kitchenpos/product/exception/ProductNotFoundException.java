package kitchenpos.product.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException() {
        super("일치하는 상품이 없습니다.");
    }
}
