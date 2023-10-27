package kitchenpos.application.request;

public class MenuProductRequest {
    private final Long productId;
    private final Integer quantity;

    public MenuProductRequest(final Long productId, final Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
