package kitchenpos.menu.ui.dto.request;

public class MenuProductCreateRequest {

    private Long productId;
    private long quantity;

    public MenuProductCreateRequest() {
    }

    public MenuProductCreateRequest(final Long productId, final long quantity) {
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
