package kitchenpos.application.dto.request;

public class MenuProductRequest {

    private long productId;
    private long quantity;

    public MenuProductRequest() {
    }

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
