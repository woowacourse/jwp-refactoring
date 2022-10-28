package kitchenpos.ui.dto;

public class CreateMenuProductsRequest {

    private Long productId;
    private long quantity;

    public CreateMenuProductsRequest() {
    }

    public CreateMenuProductsRequest(final Long productId, final long quantity) {
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
