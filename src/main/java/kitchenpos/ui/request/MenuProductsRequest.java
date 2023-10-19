package kitchenpos.ui.request;

public class MenuProductsRequest {

    private Long productId;
    private long quantity;

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setProductId(final Long productId) {
        this.productId = productId;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }
}
