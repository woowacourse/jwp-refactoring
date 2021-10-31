package kitchenpos.application.dto.request;

public class MenuProductRequest {
    private final Long productId;
    private final Long quantity;

    public MenuProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
