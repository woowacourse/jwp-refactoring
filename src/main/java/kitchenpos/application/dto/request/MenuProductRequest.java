package kitchenpos.application.dto.request;

public class MenuProductRequest {

    private final Long productId;
    private final long quantity;

    public MenuProductRequest(final long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
