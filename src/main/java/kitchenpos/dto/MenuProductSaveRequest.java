package kitchenpos.dto;

public class MenuProductSaveRequest {

    private Long productId;
    private long quantity;

    private MenuProductSaveRequest() {
    }

    public MenuProductSaveRequest(final Long productId, final long quantity) {
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
