package kitchenpos.exception;

public class ProductNotFoundException extends BusinessException {
    public ProductNotFoundException(Long productId) {
        super(String.format("%d product is not exist.", productId));
    }
}
