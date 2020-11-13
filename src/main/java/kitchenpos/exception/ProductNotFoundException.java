package kitchenpos.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long productId) {
        super("아이디가 " + productId + "인 Product를 찾을 수 없습니다!");
    }
}
