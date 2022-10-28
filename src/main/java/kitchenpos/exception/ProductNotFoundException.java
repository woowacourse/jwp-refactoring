package kitchenpos.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException() {
        super("저장되지 않은 제품을 사용할 수 없습니다.");
    }
}
