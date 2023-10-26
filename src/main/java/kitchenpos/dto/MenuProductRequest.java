package kitchenpos.dto;

public class MenuProductRequest {

    private final Long productId;
    private final long quantity;

    public MenuProductRequest(
            final Long productId,
            final long quantity
    ) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
