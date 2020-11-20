package kitchenpos.exception;

public class ProductNotExistException extends RuntimeException {
    public ProductNotExistException() {
        super("상품이 존재하지 않습니다");
    }
}
