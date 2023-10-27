package kitchenpos.product.dto;

public class ValidateExistProductEvent {

    private final Long productId;

    public ValidateExistProductEvent(final Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
