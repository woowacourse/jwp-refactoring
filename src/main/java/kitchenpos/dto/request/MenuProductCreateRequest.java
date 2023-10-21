package kitchenpos.dto.request;

public class MenuProductCreateRequest {

    private final Long productId;
    private final Long quantity;

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
