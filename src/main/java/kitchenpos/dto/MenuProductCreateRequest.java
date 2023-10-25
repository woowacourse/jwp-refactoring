package kitchenpos.dto;

public class MenuProductCreateRequest {

    private long productId;
    private long quantity;

    public MenuProductCreateRequest() {
    }

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
