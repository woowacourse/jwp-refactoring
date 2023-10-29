package kitchenpos.ui.request;

public class MenuProductCreateRequest {

    private final long productId;
    private final long quantity;

    public MenuProductCreateRequest(final long productId, final long quantity) {
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
